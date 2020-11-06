# Annonces

## How can we run the Application ?
 To run our code you have to follow these steps:
* Step One: 
``` You have to download docker PS (the version we are using is 19.03.13) then you have to download also docker-compose PS (the version we are using is 1.27.4).```
* Step Two:
``` Once step one is finished, you have to download the image of openjdk and postgres in docker.```
* Step Three:
``` And now you only have to do a bash run.sh and the server and the database containers will be launched.```
* Step Four: 
``` You can run the .jar of the client and start testing our application.```
* Step Five: 
``` You can run the rin.sh to undo what run.sh did .```

## Useful Commands 
* Connect to the database container:
```bash
	psql -h yourIP  -p 5432 -U postgres -W
```

* Afficher utilisateur du port :
```bash
	sudo lsof -i :5432
```

* Lancer interactive avec un container:
```bash
	docker exec -it db bash
```

* Utile jdbc:
```
	http://zetcode.com/java/postgresql/
```

## Contributing
* [Kouadri Achraf](https://github.com/kouadri31)
* [Khengui Salim](https://github.com/Bagniz)
