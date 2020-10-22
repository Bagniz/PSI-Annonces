FROM openjdk as client
WORKDIR /
ADD app/Client.jar Client.jar
ADD config/serverConfig.txt serverConfig.txt
CMD ["java","-jar","Client.jar"]

FROM openjdk as server
WORKDIR /
ADD app/Server.jar Server.jar
ADD config/dbConfig.txt dbConfig.txt
CMD ["java","-jar","Server.jar"]

FROM postgres as db
COPY Database/createTables.sql /docker-entrypoint-initdb.d/
COPY Database/createFunctions.sql /docker-entrypoint-initdb.d/
COPY Database/createTriggers.sql /docker-entrypoint-initdb.d/