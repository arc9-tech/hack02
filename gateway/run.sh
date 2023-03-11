#!/bin/bash
#source ../environment/gateway.env

readarray -t lines < ../environment/gateway.env
for line in "${lines[@]}"; do
  export "$line"
done

#echo $API_DOC_USER
#echo $API_DOC_PASSWORD
#echo $SSL_KEY_STORE_PASSWORD
#exec java -jar target/gateway-0.0.1-SNAPSHOT-spring-boot.jar


exec java -javaagent:../elastic-apm-agent-1.22.0.jar \
     -Delastic.apm.service_name=gateway \
     -Delastic.apm.server_urls=${APM_SERVER_URL} \
     -Delastic.apm.secret_token=${APM_SERVER_SECRET}\
     -Delastic.apm.application_packages=tech.arc9.gateway \
     -jar target/gateway-0.0.1-SNAPSHOT-spring-boot.jar