# GCR distroless image
FROM gcr.io/distroless/java:8

# copy jar into image
COPY target/gateway-0.0.1-SNAPSHOT-spring-boot.jar /gateway.jar
COPY ../elastic-apm-agent-1.22.0.jar /elastic-apm-agent-1.22.0.jar

EXPOSE 8080

# run application with this command line
CMD ["-Xmx1024m", "-javaagent:../elastic-apm-agent-1.22.0.jar", "-Delastic.apm.service_name=gateway", "-Delastic.apm.server_urls=${APM_SERVER_URL}", "-Delastic.apm.secret_token=${APM_SERVER_SECRET}", "-Delastic.apm.application_packages=tech.arc9.gateway", "-jar", "-Dspring.profiles.active=default", "/gateway.jar"]
