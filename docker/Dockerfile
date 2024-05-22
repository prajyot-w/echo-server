FROM tomcat:9
COPY echo-server-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
COPY cert.p12 /tls_key_store/cert.p12
COPY server.xml /usr/local/tomcat/conf/server.xml