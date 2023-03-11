FROM gcr.io/distroless/java:8

# copy jar into image
COPY target/user-0.0.1-SNAPSHOT-spring-boot.jar /user-service.jar

EXPOSE 8082

# run application with this command line
CMD ["-Xmx1024m", "-jar", "-Dspring.profiles.active=default", "/user-service.jar"]
