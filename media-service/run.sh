#!/bin/bash

readarray -t lines < ../environment/media.env
for line in "${lines[@]}"; do
  export "$line"
done

exec java -jar target/media-service-0.0.1-SNAPSHOT-spring-boot.jar
