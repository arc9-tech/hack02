#!/bin/bash
#source ../environment/gateway.env

readarray -t lines < ../environment/gateway.env
for line in "${lines[@]}"; do
  export "$line"
done

#echo $API_DOC_USER
#echo $API_DOC_PASSWORD
#echo $SSL_KEY_STORE_PASSWORD
exec java -jar target/gateway-0.0.1-SNAPSHOT-spring-boot.jar
