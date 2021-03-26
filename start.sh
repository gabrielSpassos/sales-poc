#!/bin/bash

echo "Build Project"
cd sales-service
./gradlew clean build

echo "Start Project"
cd ..
docker-compose build
docker-compose up
