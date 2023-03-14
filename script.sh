#!/bin/bash
PROJECT="welcome-to-esprit"
if [ -d $PROJECT ]; then
    echo $PROJECT exists.
    cd $PROJECT
    git fetch origin main
    git pull origin main
else
    git clone git@gitlab.com:ahmedg99/welcome-to-esprit.git
    cd $PROJECT
fi
#mvn clean package -Dmaven.test.skip
docker-compose down -v --remove-orphans
docker-compose up --build -d
docker rmi $(docker images --filter "dangling=true" -q --no-trunc)
docker builder prune -a -f
sleep 5
docker ps -a
echo "Good Luck, boy-boy!"
