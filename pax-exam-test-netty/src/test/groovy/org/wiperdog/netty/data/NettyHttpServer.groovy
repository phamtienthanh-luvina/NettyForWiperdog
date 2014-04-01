package org.wiperdog.netty.data;

import net.javaforge.netty.servlet.bridge.ServletBridgeChannelPipelineFactory;
import net.javaforge.netty.servlet.bridge.config.ServletConfiguration;
import net.javaforge.netty.servlet.bridge.config.WebappConfiguration;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class NettyHttpServer {

    private static final Logger log = LoggerFactory.getLogger(NettyHttpServer.class);

    public static void startServer() {
    	int port = 8080;
        long start = System.currentTimeMillis();

        // Configure the server.
        final ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(Executors
                        .newCachedThreadPool(), Executors.newCachedThreadPool()));

        //Registering servlet in servlet container
 
        WebappConfiguration webapp = new WebappConfiguration();
        webapp.addServletConfigurations(new ServletConfiguration(
                TestServlet.class, "/servlet/*").addInitParameter("params1",
                "value1"));
        webapp.addServletConfigurations(new ServletConfiguration(
                TestServlet2.class, "/servlet2/*").addInitParameter("params2",
                "value2"));
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
