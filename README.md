# Ab-Inbev Code Challenge

Ab-Inbev code challenge assessment.

## Installation

In this section are presented the steps to install the application using docker-compose.

> Pre-requisites: Have installed docker and docker-compose on your machine.

### Configuring docker volumes
1. In the docker-compose.yml file configure the local path to be used as volumes for services: db-postgresql, db-pgadmin and ab-inbev_app.
2. Give permission to the local path so we avoid having issues related to that. Example on Linux:

```
chmod -R 777 <local_volume_path>
``` 

### Application Hints and Tips
We're using docker to deliver the application.

The Product service uses Postgresql as RDBMS.

Before starting the application stack make sure the ports listed below are not in use:
* 8081 - PgAdmin server;
* 5433 - Postgres server;
* 8083 - Product Application.

In case you need to change local ports, just open the file **.env** in the **<repository_root_directory>/ab-inbev_stack** and change the below properties:
* POSTGRESQL_HOST_PORT - Defines the host port for Postgres container;
* PRODUCT_HOST_PORT - Defines the host port for Product Application container;
* PGADMIN_HOST_PORT - Defines the host port for PgAdmin server.

### Starting the application

1. Go to the repository root directory;
2. Open the directory **ab-inbev_stack**
3. Execute the command below to start the application
```
docker-compose up -d
```

## Api Documentation
Use the below URL to access the application api documentation.

http://localhost:8083/swagger-ui.html

Don't forget to change the **port** if you changed it. 

The REST apis are secured with Sprint Security and JWT Token.

For demonstration purposes use the credentials below to authenticate as a regular user:

**user**: javainuse

**password**: password

To authenticate please the **jwt-authentication-controller** item from swagger ui.

Now to use the product apis use the **product-controller** item from swagger ui. Don't forget to fill the Bearer token, otherwise you won't be able to use the APIs. To do that press the lock sign available in any api under **product-controller** and in the opened modal type the value:

```
Bearer <authentication_token>
```


## Jenkins File
Created a Jenkins pipeline for CI to execute the build, test, image build and image deploy to docker-hub.


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)