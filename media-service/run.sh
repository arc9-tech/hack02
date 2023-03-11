#!/bin/bash

readarray -t lines < ../environment/media.env
for line in "${lines[@]}"; do
  export "$line"
done

#exec java -jar target/media-service-0.0.1-SNAPSHOT-spring-boot.jar

exec java -javaagent:../elastic-apm-agent-1.22.0.jar \
     -Delastic.apm.service_name=mediaservice \
     -Delastic.apm.server_urls=${APM_SERVER_URL} \
     -Delastic.apm.secret_token=${APM_SERVER_SECRET}\
     -Delastic.apm.application_packages=tech.arc9.mediaservice \
     -jar target/media-service-0.0.1-SNAPSHOT-spring-boot.jar