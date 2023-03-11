FROM gcr.io/distroless/java:8

# copy jar into image
COPY target/media-service-0.0.1-SNAPSHOT-spring-boot.jar /media-service.jar

EXPOSE 8080

# run application with this command line
CMD ["-Xmx1024m", "-jar", "-Dspring.profiles.active=default", "/media-service.jar"]