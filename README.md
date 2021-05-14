# Steps to run application

1. Create a container for running MYSQL server:
```
    sudo docker run --name mysql -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_USER=springuser -e MYSQL_PASSWORD=1234 -e MYSQL_DATABASE=db_example -d mysql:latest
```
2. Get the IP address of the container:
```
    sudo docker inspect CONTAINER_ID;
```
3. Change the IP_ADDRESS in the application.properties file for the container's IP:
```
    spring.datasource.url=jdbc:mysql://IP_ADDRESS:3306/db_example
```
4. Create a container for RabbitMQ: 
```
    docker run -d  --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```
5. Launch Application.

* To test the requests on Postman, first execute the "Get User's token" and copy the TOKEN send by the server into the next header's requests.
* Authorzation: Bearer TOKEN
* Postman Collection: https://www.getpostman.com/collections/0910669a72c0c00f1395