#!/bin/bash
mvn clean package -Dmaven.test.skip
docker-compose up --build -d
docker rmi $(docker images --filter "dangling=true" -q --no-trunc)
sleep 5
docker ps -a
echo "Good Luck, boy-boy!"
