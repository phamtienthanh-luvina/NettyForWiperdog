NettyForWiperdog
================

Temporary repository for stuffs about using netty on wiperdog

This project using netty-servlet-bridge .An open source project for implement Servlet API on the Netty (https://github.com/bigpuritz/netty-servlet-bridge)

 - pax-exam-test-netty : 
   The test project for netty-servlet-bridge .Need to do unit test for funtions of this project ,if it lack functions or have problems ,we need to append or fix 
 - wiperdog : 
   Source code for demo using netty-servlet-bridge in wiperdog 


About wiperdogInstaller :
- This is installer version of wiperdog ,replace jetty by netty ,following is the change logs :
	+ Remove all jetty's bundle in etc/ListBundle.csv 
	
    		mvn,org.eclipse.jetty:jetty-client:7.6.8.v20121106,2,
    		mvn,org.eclipse.jetty:jetty-continuation:7.6.8.v20121106,2,
    		mvn,org.eclipse.jetty:jetty-deploy:7.6.8.v20121106,2,
    		mvn,org.eclipse.jetty:jetty-http:7.6.8.v20121106,2,
    		mvn,org.eclipse.jetty:jetty-io:7.6.8.v20121106,2,
    		mvn,org.eclipse.jetty:jetty-security:7.6.8.v20121106,2,
    		mvn,org.eclipse.jetty:jetty-server:7.6.8.v20121106,2,
    		mvn,org.eclipse.jetty:jetty-servlet:7.6.8.v20121106,2,
    		mvn,org.eclipse.jetty:jetty-util:7.6.8.v20121106,2,
    		mvn,org.eclipse.jetty:jetty-webapp:7.6.8.v20121106,2,
    		mvn,org.eclipse.jetty:jetty-xml:7.6.8.v20121106,2,

	+ Using netty-servlet-brigde in wiperdog
		 + Add new bundle/jar files to etc/ListBundle.csv
		 
     			mvn,org.jboss.netty:netty:3.2.6.Final,2,
     			wrapfile,lib/java/bundle.wrap/netty-servlet-bridge-1.0.0-SNAPSHOT.jar,3,

		 + Modified  src/main/assemble/component.xml to add bundle netty-servlet-bridge-1.0.0-SNAPSHOT.jar

		 + Place netty-servlet-bridge-1.0.0-SNAPSHOT.jar to lib/java/bundle.wrap

		*Note : the netty-servlet-bridge is modified a litle of code to run in wiperdog ,check modified source code in :
		https://github.com/nguyenxuanluong-luvina/NettyDemo/tree/master/netty-servlet-bridge
	+ Remove groovy/libs.common/JettyLoader.groovy by groovy/libs.common/NettyLoader.groovy
	+ Change etc/boot.groovy to load groovy/libs.common/NettyLoader.groovy (line : 104)
	+ Modified src/main/resources/installer/installer.groovy to replace  input jetty config  by netty config
	+ Remove opt/jetty
