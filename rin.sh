#!/bin/sh
docker stop $(docker ps -aq)
docker rm $(docker ps -aq) 
docker image rm client 
docker image rm server 
docker image rm db
docker rmi $(docker images --filter "dangling=true" -q --no-trunc)
docker volume rm $(docker volume ls -q)
sudo kill $(sudo lsof -t -i:5432)
