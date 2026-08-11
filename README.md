# Description
A microservice for managing projects and tasks
# Features
- create projects and tasks in these projects
- delete projects and tasks
- update info: title, description, status, priority
- get projects and tasks (or a project and a task)
# Stack
## Main stack
- Language: Java 21
- Framework: Spring boot 4.0.7
- DBMS: PostgreSQL 16
- Build tool: Maven 4.0.0
- API documentation: SpringDoc OpenAPI UI 3.0.2
## Dependencies
- Spring Data JPA
- Spring Web MVC
- SpringDoc OpenAPI UI
- Spring Boot DevTools
- Spring Boot Docker Compose
- Lombok
- Spring Data JPA Test
- PostgreSQL JDBC Driver
- Spring Boot Starter Test
- Spring Boot Testcontainers
- Testcontainers JUnit Jupiter
- Testcontainers PostgreSQL
# How to launch
## Production Deployment
For the first you need to have docker and docker compose installed, and docker engine have to be ran.

Then you go:

1. Clone the repository:
``` Bash
git clone https://github.com/Simpav-chill/tasktracker.git
```
2. Change directory to the project directory:
``` Bash
cd tasktracker 
```
3. Make .evn file in the root directory of the project and write there database login and password like this:
```
DB_URL=jdbc:postgresql://youdatabaseaddress
DB_USERNAME=yourdblogin
DB_PASSWORD=yourdbpassword
```
4. Then save it and run in the console (terminal on Linux/MacOS or cmd/powershell on Windows):

If you need only Spring boot app:
``` Bash
cd yourpathtotheproject
docker-compose -f compose.prod.yaml up -d
```

If you need Spring boot app and PostgreSQL database together:
``` Bash
cd youpathtotheproject
docker-compose -f compose.full.yaml up -d
```

5. Check whether it works (You'll see containers if it works. Number of them depends on the choice you made in the previous step):
``` Bash
docker-compose ps
```
## Development
For the first you need to have docker and docker compose installed, and docker engine have to be ran.

Then you go:

1. Clone the repository:
``` Bash
git clone https://github.com/Simpav-chill/tasktracker.git
cd tasktracker
```
2. Make .evn file in the root directory of the project and write there database login and password like this:
```
DB_URL=jdbc:postgresql://youdatabaseaddress
DB_USERNAME=yourdblogin
DB_PASSWORD=yourdbpassword
```
3. Then save it and run in the console (terminal on Linux/MacOS or cmd/powershell on Windows):

If you need Spring boot app launched locally (not in container):

Launch database in container:
``` Bash
cd yourpathtotheproject
docker-compose -f compose.yaml up -d
```

After that if you use IntelliJ IDEA, follow the steps:
1. Open Run - Edit Configurations...
2. Choose Spring boot - TasktrackerApplication
3. Type 'local' in the 'Active profiles' field
4. Apply changes and close this window
5. Run TasktrackerApplication.java

If you need Spring boot app and PostgreSQL database together launched in containers:
``` Bash
cd youpathtotheproject
docker-compose -f compose.full.yaml up -d
```
# API Documentation
Launch the app and then visit:
```
http://localhost:8080/swagger-ui/index.html
```
# Project structure
**src.main.java.com.example.tasktracker:**
- config - contains Jpa config class
- controller - contains Project and Task controllers
- dto - contains request and response DTOs (e.g. UpdateTaskDto or ProjectDto)
- entity - contains Project and Task entities
- exception - contains custom exceptions and global exception handler
- mapper - contains Task and Project mappers
- repository - contains Task and Project repositories
- service - contains Task and Project service classes
- TasktrackerApplication.java - app entry point

**src.main.resources:**
- application-docker.yaml - contains properties for launching the app and database from one docker compose file, and can be used both for development and production
- application-local.yaml - contains properties for launching database in docker container and the app locally; it's created for developing
- application-prod.yaml - contains properties for launching the app in docker container, it's designed for production deployment
- application.yaml - contains common properties for all types of launching

**src.test.java.com.example.tasktracker**:
- integration - contains integration tests for Task and Project controllers
- unit - contains unit tests for Task and Project service classes

**src.test.resources:**
- application-test.yaml - contains properties for hibernate to be able to test controllers

**root directory:**
- Dockerfile - dockerfile for the app
- compose.full.yaml - launches containers for the app and database, and can be used both for development and production
- compose.prod.yaml - launches container for the app only, designed for production deployment
- compose.yaml - launches for container for database only, designed for development
- pom.xml - contains all info about the app to build it, e.g. dependencies, meta data, etc
# Database structure
- Project:
    - id bigint auto increment primary key
    - title varchar 255
    - description varchar 255
    - status smallint
    - created_at timestamp 6
    - updated_at timestamp 6
- Task:
    - id bigint auto increment primary key
    - title varchar 255
    - description varchar 255
    - status smallint
    - priority smallint
    - created_at timestamp 6
    - updated_at timestamp 6
    - project_id bigint foreign key
