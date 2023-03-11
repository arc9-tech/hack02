#!/bin/bash

readarray -t lines < ../environment/user.env
for line in "${lines[@]}"; do
  export "$line"
done

#echo $MYSQL_DATABASE

#exec java -jar target/user-0.0.1-SNAPSHOT-spring-boot.jar


exec java -javaagent:../elastic-apm-agent-1.22.0.jar \
     -Delastic.apm.service_name=user \
     -Delastic.apm.server_urls=${APM_SERVER_URL} \
     -Delastic.apm.secret_token=${APM_SERVER_SECRET}\
     -Delastic.apm.application_packages=tech.arc9.user \
     -jar target/user-0.0.1-SNAPSHOT-spring-boot.jar