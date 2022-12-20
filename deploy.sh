#!/bin/bash
./gradlew.bat build
docker image build . -t backend:latest
docker tag backend:latest onexx/backend:latest
docker push onexx/backend:latest
echo ------------------
echo Deployed successfully, now you need to run ./deploybackend on the server
read -p "Press any key to resume ..."