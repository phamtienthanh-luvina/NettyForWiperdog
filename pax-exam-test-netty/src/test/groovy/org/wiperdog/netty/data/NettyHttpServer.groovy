package org.wiperdog.netty.data;

import net.javaforge.netty.servlet.bridge.ServletBridgeChannelPipelineFactory;
import net.javaforge.netty.servlet.bridge.config.ServletConfiguration;
import net.javaforge.netty.servlet.bridge.config.WebappConfiguration;

import org.codehaus.groovy.control.CompilationFailedException;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.GroovyClassLoader;
import groovy.io.FileType;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NettyHttpServer {
	ClassLoader loader ;
	ServerBootstrap bootstrap;
	ServletBridgeChannelPipelineFactory servletBridge;
	Channel serverChannel;
	private static final Logger log = LoggerFactory
	.getLogger(NettyHttpServer.class);
	public NettyHttpServer(ClassLoader loader){
		this.loader = loader;
	}
	public void startServer(String servletDir) throws CompilationFailedException, IOException {
		int port = 8080;

		// Path to folder contains servlet
		File dirServlet = new File(servletDir);
		File[] listAllServlet = dirServlet.listFiles();

		long start = System.currentTimeMillis();

		// Configure the server.
		bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));

		// Registering servlet in servlet container
		WebappConfiguration webapp = new WebappConfiguration();
		if (dirServlet.exists()) {
			dirServlet.eachFileRecurse(FileType.FILES) { file ->
				if(file.name.endsWith(".groovy")){
					def groovyClassLoader = new GroovyClassLoader(this.loader)
					def servletClazz;
					try{
						servletClazz = groovyClassLoader.parseClass(new File(file.getAbsolutePath()))
					}catch(Exception ex) {
						println "Failed to load servlet from file : " + file
						ex.printStackTrace();
					}
					if(servletClazz != null){
						webapp.addServletConfigurations(new ServletConfiguration(servletClazz, "/" + servletClazz.getSimpleName() + "/"));
					}
				}
			}
		}else{
			println "Servlet folder not found :  " + dirServlet.getAbsolutePath()
			
		}

		// Set up the event pipeline factory.
		servletBridge = new ServletBridgeChannelPipelineFactory(
				webapp);
		bootstrap.setPipelineFactory(servletBridge);

		// Bind and start to accept incoming connections.
		serverChannel = bootstrap
				.bind(new InetSocketAddress(port));

		long end = System.currentTimeMillis();
		log.info(">>> Server started in {} ms .... <<< ", (end - start));
	}
	public void stopServer(){
		if(servletBridge != null) {
			servletBridge.shutdown();
			serverChannel.close().awaitUninterruptibly();
			bootstrap.releaseExternalResources();
		}
	}
}
