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

	private static final Logger log = LoggerFactory
			.getLogger(NettyHttpServer.class);

	public static void startServer() throws CompilationFailedException, IOException {
		int port = 8080;
		
		// Path to folder contains servlet
        File dirServlet = new File("/org/wiperdog/netty/data/servlet/");
        
		long start = System.currentTimeMillis();

		// Configure the server.
		final ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Registering servlet in servlet container
		WebappConfiguration webapp = new WebappConfiguration();
		if (dirServlet.exists()) {
			dirServlet.eachFileRecurse(FileType.FILES) { file ->
				def servletClass = new GroovyClassLoader().parseClass(new File(file.getAbsolutePath()))
				webapp.addServletConfigurations(new ServletConfiguration(servletClass, "/" + servletClass.getSimpleName() + "/*"));
			}
		}
		
		// Set up the event pipeline factory.
		final ServletBridgeChannelPipelineFactory servletBridge = new ServletBridgeChannelPipelineFactory(
				webapp);
		bootstrap.setPipelineFactory(servletBridge);

		// Bind and start to accept incoming connections.
		final Channel serverChannel = bootstrap
				.bind(new InetSocketAddress(port));

		long end = System.currentTimeMillis();
		log.info(">>> Server started in {} ms .... <<< ", (end - start));

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				servletBridge.shutdown();
				serverChannel.close().awaitUninterruptibly();
				bootstrap.releaseExternalResources();
			}
		});
	}
}
