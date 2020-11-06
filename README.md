# Annonces
A sales application between individuals
## Getting Started
These instructions will get you a copy of the project up and running on your local machine for testing purposes.

### Prerequisites
To run our code you have to install the following technologies on your local machine:

1. [Docker](https://docs.docker.com/) at least version `19.03.13`, if you are on linux you just need to execute the following teminal commands:
	```bash
		$ sudo apt-get update
		$ sudo apt-get install docker-ce docker-ce-cli containerd.io
	```

1. [Docker-Compose](https://docs.docker.com/compose/) at least version `1.27.4`, if you are on linux you just need to execute the following teminal commands:
	```bash
		$ sudo curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)
			-$(uname -m)" -o /usr/local/bin/docker-compose
		$ sudo chmod +x /usr/local/bin/docker-compose
	```
1. [Openjdk image](https://hub.docker.com/_/openjdk) using the following command ``` $ sudo docker pull openjdk ```
1. [Postgresql image](https://hub.docker.com/_/postgres) using the following command ``` $ sudo docker pull postgres ```

### Running the app
Now you have to follow the following instructions to run the app at the root of out projets structure

1. run ``` $ bash run.sh ```, this will allow the server and database containers to be launched.
2. run these commands to launch the clients app.

	```
	$ cd app/
	$ java -jar Client.jar 
	```
3. click on `ctrl + c` to stop the sever and database containers.
4. run ``` $ sudo bash rin.sh ```, this will undo what `run.sh` did, and delete the server and database containers.

<div style="page-break-after: always;"></div>

## Tests
When the server and database containers are launched, there would be aleady some data inserted so you can execute the client app and do some operations

Here are some users login to test with if you don't wanna sign up yet:
```
	Email: userone@gmail.com
	Password: 1234

	Email: usertwo@gmail.com
	Password: 1234

	Email: userthree@gmail.com
	Password: 1234

	Email: userfour@gmail.com
	Password: 1234

	Email: userfive@gmail.com
	Password: 1234

	Email: usersix@gmail.com
	Password: 1234
	
	Email: userseven@gmail.com
	Password: 1234
	
	Email: usereight@gmail.com
	Password: 1234

	Email: usernine@gmail.com
	Password: 1234

	Email: userten@gmail.com
	Password: 1234

	Email: usereleven@gmail.com
	Password: 1234

	Email: usertwelve@gmail.com
	Password: 1234

	Email: userthirteen@gmail.com
	Password: 1234

	Email: userfourteeg@gmail.com
	Password: 1234

	Email: userfifteen@gmail.com
	Password: 1234
```

## Useful Commands 
* Connect to the database container:
```bash
	$ psql -h yourIP  -p 5432 -U postgres -W
```

* Afficher utilisateur du port :
```bash
	$ sudo lsof -i :5432
```

* Lancer interactive avec un container:
```bash
	$ docker exec -it db bash
```

## Technologies

* [OpenJDK](https://openjdk.java.net/)
* [Docker](https://docs.docker.com/)
* [Docker-Compose](https://docs.docker.com/compose/)
* [Openjdk docker image](https://hub.docker.com/_/openjdk)
* [Postgresql docker image](https://hub.docker.com/_/postgres)
* [Postgresql](https://www.postgresql.org/)
* [Postgresql JDBC](http://zetcode.com/java/postgresql/)

## Contributing
* [Kouadri Achraf](https://github.com/kouadri31)
* [Khengui Salim](https://github.com/Bagniz)
