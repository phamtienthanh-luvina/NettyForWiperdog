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

	@Before
	public void prepare() {
		if(!server_start) {
			NettyHttpServer server = new NettyHttpServer();
			server.startServer();
			server_start = true			
		}
	}

	@After
	public void finish() {

	}

	@Test
	public void TestNetty1() {
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/servlet");
		listCmd.add("-v");
		ProcessBuilder pb = new ProcessBuilder(listCmd);
		pb.redirectErrorStream(true);
		try {
			Process proc = pb.start();
			BufferedReader bf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			String result = ""
			while ((line = bf.readLine()) != null) {
				result += line;
			}
			assertTrue(result.contains("TestServlet work params1 : value1"))
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	@Test
	public void TestNetty2() {
		List<String> listCmd = new ArrayList<String>();
		listCmd.add("curl");
		listCmd.add("http://localhost:8080/servlet2?param=value");
		listCmd.add("-v");
		ProcessBuilder pb = new ProcessBuilder(listCmd);
		pb.redirectErrorStream(true);
		try {
			Process proc = pb.start();
			BufferedReader bf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			String result = ""
			while ((line = bf.readLine()) != null) {
				result += line + "\n";
			}
			println result
			assertTrue(result.contains("TestServlet2 work params2 : value2"))
			assertTrue(result.contains("TestServlet2 url params: value"))
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
