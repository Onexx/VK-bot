call gradlew build
call docker image build . -t backend:latest
call docker tag backend:latest onexx/backend:latest
call docker push onexx/backend:latest
echo ------------------
echo Deployed successfully, now you need to run ./deploybackend on the server