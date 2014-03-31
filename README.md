NettyForWiperdog
================

Temporary repository for stuffs about using netty on wiperdog

This is demo project for using netty to creat a httpserver + servlet handler 
This project depend on the netty-servlet-bridge - An open source project for implement Servlet API on the Netty (https://github.com/bigpuritz/netty-servlet-bridge)

 How to run demo : 
	 -  Start a httpserver : 
		mvn exec:java -Dexec.mainClass=example.netty.TestHttpServerAndServletHandler
	 -  In another terminal console ,run test (send a http request to servlet hosted in server) : 
		mvn test
	 -  Check response from test result (print out to console)

