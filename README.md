# VK-bot
A simple VK bot written in kotlin for a PHP course at ITMO University.

The bot allows you to create and manage your own schedule by creating and deleting tasks with a set time and repeat frequency.

## Building and Deploy
The project is built with Gradle.  Run Gradle to build the project using the following command:
```
./gradlew build
```
The code also contains semi-automated deployment scripts `deploy.bat` and `deploy.sh` that contain commands to build the project and push it to Docker Hub.

The following environment variables are required to run the project:
```
DATABASE_URL
DATABASE_USER
DATABASE_PASSWORD
GROUP_ID
ACCESS_TOKEN
```
## Functionality
 - create new task with custom date, time, repeat and text
 - list all created tasks
 - list tasks for current day
 - list tasks for surrent week
 - remove unwanted task

## Technologies used
 - Gradle
 - MariaDB + MyPHPAdmin
 - Guava
 - [vk-sdk-kotlin](https://github.com/vksdk/vk-sdk-kotlin) + OkHttp
 
