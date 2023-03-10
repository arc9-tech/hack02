#!/bin/bash

readarray -t lines < ../environment/user.env
for line in "${lines[@]}"; do
  export "$line"
done

#echo $MYSQL_DATABASE

exec java -jar target/user-0.0.1-SNAPSHOT-spring-boot.jar
