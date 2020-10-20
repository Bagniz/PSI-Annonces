#!/bin/sh
sudo docker build --target serveur -t serveur .
sudo docker build --target client -t client .
sudo docker build --target db -t db .
sudo docker-compose build
sudo docker-compose up
