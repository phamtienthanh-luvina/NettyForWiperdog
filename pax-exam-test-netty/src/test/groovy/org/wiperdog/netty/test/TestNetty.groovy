package org.wiperdog.netty.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;

import javax.inject.Inject;

import static org.junit.Assert.*;
import static org.ops4j.pax.exam.CoreOptions.*;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.junit.runner.JUnitCore;
import org.osgi.service.cm.ManagedService;
import org.wiperdog.netty.data.NettyHttpServer;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class TestNetty {
	public TestNetty() {
	}

	@Inject
	private org.osgi.framework.BundleContext context;
	@Configuration
	public Option[] config() {

		return options(
		cleanCaches(true),
		frameworkStartLevel(6),
		// felix log level
		systemProperty("felix.log.level").value("4"), // 4 = DEBUG
		// setup properties for fileinstall bundle.
		systemProperty("felix.home").value(System.getProperty("user.dir")),
		// Pax-exam make this test code into OSGi bundle at runtime, so
		// we need "groovy-all" bundle to use this groovy test code.
		mavenBundle("org.codehaus.groovy", "groovy-all", "2.2.1").startLevel(2),
		//mavenBundleWrapper("org.codehaus.groovy", "groovy-all", "2.2.1").startLevel(2),,
		wrappedBundle(mavenBundle("javax.servlet", "servlet-api", "2.5")),
		wrappedBundle(mavenBundle("io.netty", "netty", "3.6.2.Final")),
		wrappedBundle(mavenBundle("net.javaforge.netty", "netty-servlet-bridge", "1.0.0-SNAPSHOT")),
		junitBundles()
		);
	}
	static server_start = false;
	NettyHttpServer server;
	@Before
	public void prepare() {
		server = new NettyHttpServer(this.getClass().getClassLoader());
	}

	@After
	public void finish() {
		server.stopServer();
	}
	/**
	 * 1 Servlet load success and can process request
	 */
	@Test
	public void TestLoadServlet1() {
		server.startServer('src/test/resources/servlet/TestLoadServlet1');
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/TestServlet" );
		listCmd.add("-v");
		def result = this.sentRequest(listCmd)
		assertTrue(result.contains("TestServlet loaded successfully"))
	}

	/**
	 * 1 Servlet load unsuccess but server still can be started 
	 * The error return is :Failure: 500 Internal Server Error 
	 */
	@Test
	public void TestLoadServlet2() {
		server.startServer('src/test/resources/servlet/TestLoadServlet2');
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/TestServlet2" );
		listCmd.add("-v");
		def result = this.sentRequest(listCmd)
		assertTrue(result.contains("Failure: 500 Internal Server Error"))
	}
	/**
	 * Multi servlet loaded and will serve for multi request 
	 */
	@Test
	public void TestLoadServlet3() {
		server.startServer('src/test/resources/servlet/TestLoadServlet3');
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/TestServlet3_1" );
		listCmd.add("-v");
		def result = this.sentRequest(listCmd)
		assertTrue(result.contains("TestServlet3_1 loaded successfully"))
		listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/TestServlet3_2" );
		listCmd.add("-v");
		result = this.sentRequest(listCmd)
		assertTrue(result.contains("TestServlet3_2 loaded successfully"))
	}

	/**
	 * Multi servlet loaded ,1 servlet failed to load but server still can be started
	 * and other loaded servlet will be serve
	 */
	@Test
	public void TestLoadServlet4() {
		server.startServer('src/test/resources/servlet/TestLoadServlet4');
		//sent request to servlet not available
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/TestServlet4_1" );
		listCmd.add("-v");
		def result = this.sentRequest(listCmd)
		//Thread.sleep(100000)
		assertTrue(result.contains("Failure: 500 Internal Server Error"))

		//sent request to available servlet
		listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/TestServlet4_2" );
		listCmd.add("-v");
		result = this.sentRequest(listCmd)
		assertTrue(result.contains("TestServlet4_2 loaded successfully"))
	}

	/**
	 * Multi servlet loaded and will be serve for request
	 */
	@Test
	public void TestLoadServlet5() {
		server.startServer('src/test/resources/servlet/TestLoadServlet5');
		//sent request to an available servlet
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/TestServlet5_1" );
		listCmd.add("-v");
		def result = this.sentRequest(listCmd)
		//Thread.sleep(100000)
		assertTrue(result.contains("Failure: 500 Internal Server Error"))

		//sent request to another available servlet
		listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/TestServlet5_2" );
		listCmd.add("-v");
		result = this.sentRequest(listCmd)
		assertTrue(result.contains("Failure: 500 Internal Server Error"))
	}

	/**
	 * Check the init() function to be invoked by servlet container when it loaded 
	 */
	@Test
	public void TestInit() {
		File f = new File("src/test/resources/servlet/TestInit/output.txt")
		if(f.exists()){
			//remove file output before test
			f.delete()
		}
		server.startServer('src/test/resources/servlet/TestInit');
		//sent request to an available servlet
		Thread.sleep(2000)
		//When the servlet init ,a string will be write to file in init()
		def result
		if(f.exists()){
			result = f.getText();
		}
		//Thread.sleep(100000)
		assertEquals(result,"TestInitServlet init() invoked")
	}

	@Test
	/**
	 * TestDoGet1: send GET request to a valid servlet.
	 */
	public void TestGet2ValidServlet() {
		server.startServer('src/test/resources/servlet/TestProcessRequest');
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/ValidServlet?param=test_do_get_1");
		listCmd.add("-v");

		def result = this.sentRequest(listCmd);
		assertTrue(result.contains("Test doGet 1 work param: test_do_get_1"))
	}


	@Test
	/**
	 * TestDoGet2: send GET request to an invalid/not exists servlet.
	 */
	public void TestGet2InvalidServlet() {
		server.startServer('src/test/resources/servlet/TestProcessRequest');
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/InvalidServlet");
		listCmd.add("-v");

		def result = this.sentRequest(listCmd);
		assertTrue(result.contains("Failure: 500 Internal Server Error"))
	}

	@Test
	/**
	 * TestDoPost1: send POST request to a valid servlet.
	 */
	public void TestPost2ValidServlet() {
		server.startServer('src/test/resources/servlet/TestProcessRequest');
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("-X");
		listCmd.add("POST");
		listCmd.add("-H");
		listCmd.add("Content-type: application/json");
		listCmd.add("-d");
		listCmd.add('{"doPost":"nettydemo"}');
		listCmd.add("http://localhost:8080/ValidServlet");
		listCmd.add("-v");

		def result = this.sentRequest(listCmd);
		assertTrue(result.contains("nettydemo"))
	}

	@Test
	/**
	 * TestDoPost2: send POST request to an invalid/not exists servlet.
	 */
	public void TestPost2InvalidServlet() {
		server.startServer('src/test/resources/servlet/TestProcessRequest');
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("-X");
		listCmd.add("POST");
		listCmd.add("-H");
		listCmd.add("Content-type: application/json");
		listCmd.add("-d");
		listCmd.add('{"doPost":"nettydemo"}');
		listCmd.add("http://localhost:8080/InvalidServlet");
		listCmd.add("-v");

		def result = this.sentRequest(listCmd);
		assertTrue(result.contains("Failure: 500 Internal Server Error"))
	}

	@Test
	/**
	 * TestDoPut1: send PUT request to a valid servlet.
	 */
	public void TestPut2ValidServlet() {
		server.startServer('src/test/resources/servlet/TestProcessRequest');
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("-X");
		listCmd.add("PUT");
		listCmd.add("-H");
		listCmd.add("Content-type: application/json");
		listCmd.add("-d");
		listCmd.add('{"doPut":"nettydemo"}');
		listCmd.add("http://localhost:8080/ValidServlet");
		listCmd.add("-v");

		def result = this.sentRequest(listCmd);
		assertTrue(result.contains("nettydemo"))
	}

	@Test
	/**
	 * TestDoPut2: send PUT request to an invalid/not exists servlet.
	 */
	public void TestPut2InvalidServlet() {
		server.startServer('src/test/resources/servlet/TestProcessRequest');
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("-X");
		listCmd.add("PUT");
		listCmd.add("-H");
		listCmd.add("Content-type: application/json");
		listCmd.add("-d");
		listCmd.add('{"doPut":"nettydemo"}');
		listCmd.add("http://localhost:8080/InvalidServlet");
		listCmd.add("-v");
		def result = this.sentRequest(listCmd);
		assertTrue(result.contains("Failure: 500 Internal Server Error"))
	}

	/**
	 * Multi servlet loaded and will be serve for request
	 */
	@Test
	public void TestCookie() {
		server.startServer('src/test/resources/servlet/TestCookie');
		//sent request to an available servlet with cookie
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("-b");
		listCmd.add('USER_NAME=ROOT;PASSWD=ROOT123');
		listCmd.add("-c")
		listCmd.add("src/test/resources/servlet/TestCookie/servletSentCookie.txt")
		listCmd.add("http://localhost:8080/TestCookieServlet" );
		listCmd.add("-v");
		File servletReceivedCookie = new File("src/test/resources/servlet/TestCookie/servletReceivedCookie.txt")
		if(servletReceivedCookie.exists()){
			servletReceivedCookie.delete()
		}
		File servletSentCookie = new File("src/test/resources/servlet/TestCookie/servletSentCookie.txt")
		if(servletSentCookie.exists()){
			servletSentCookie.delete()
		}
		def result = this.sentRequest(listCmd)
		//Check cookie servlet received from request

		assertTrue(servletReceivedCookie.exists())
		assertTrue(servletReceivedCookie.getText().contains("USER_NAME:ROOT"))
		assertTrue(servletReceivedCookie.getText().contains("PASSWD:ROOT123"))
		//Check cookie servlet sent to request
		assertTrue(servletSentCookie.exists())
		assertTrue(servletSentCookie.exists())
		def expectedCookie = "localhost	FALSE	/	FALSE	0	USER_NAME	Wiperdog"
		assertTrue((servletSentCookie.getText().contains(expectedCookie)))
	}

	@Test
	/**
	 *
	 */
	public void TestSession() {
		server.startServer('src/test/resources/servlet/TestSession');
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/TestSession");
		listCmd.add("-v");

		def result = this.sentRequest(listCmd)
		assertTrue(result.contains("Session was created !!!"))
	}

	/**
	 * Check the destroy() function to be invoked by servlet container when server shutdown
	 */
	@Test
	public void TestDestroy() {
		File f = new File("src/test/resources/servlet/TestDestroy/output.txt")
		if(f.exists()){
			//remove file output before test
			f.delete()
		}
		server.startServer('src/test/resources/servlet/TestDestroy');
		//sent request to an available servlet
		Thread.sleep(2000)
		assertFalse(f.exists())

		//When the servlet is destroyed ,a string will be write to file in destroy()
		server.stopServer();
		Thread.sleep(2000)
		def result
		assertTrue(f.exists())
		result = f.getText();
		assertEquals(result,"TestDestroyServlet destroy() invoked")
	}

	/**
	 * Multi servlet loaded and will be serve for request
	 */
	@Test
	public void TestMultiRequest() {
		server.startServer('src/test/resources/servlet/TestMultiRequest');
		//sent request to an available servlet
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/TestMultiRequest" );
		listCmd.add("-v");
		File f = new File("src/test/resources/servlet/TestMultiRequest/instance.txt")
		if(f.exists()){
			f.delete()
		}
		//Send first request
		this.sentRequest(listCmd)
		//sleep a while to wait for server start
		Thread.sleep(2000)
		//check file written from servlet
		assertTrue(f.exists())
		def instanceHashCode = f.getText()
		assertNotNull(instanceHashCode)
		//Delete file before send 2nd request
		if(f.exists()){
			f.delete()
		}
		//Send 2nd request
		this.sentRequest(listCmd)

		//check file written from servlet
		assertTrue(f.exists())
		def instanceHashCode2 = f.getText()
		assertEquals(instanceHashCode,instanceHashCode2)
	}

	/**
	 * Sent request to server and read return data
	 */
	public String sentRequest(listCmd){
		ProcessBuilder pb = new ProcessBuilder(listCmd);
		pb.redirectErrorStream(true);
		Process proc = pb.start();
		BufferedReader bf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line = null;
		String result = ""
		while ((line = bf.readLine()) != null) {
			result += line + "\n";
		}
		println result
		return result
	}

}