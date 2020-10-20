from openjdk as client
WORKDIR /
ADD app/Client.jar Client.jar
ADD config/configClient.txt configClient.txt
CMD ["java","-jar","Client.jar"]

from openjdk as server
WORKDIR /
ADD app/Server.jar Server.jar
ADD config/configDB.txt configDB.txt
CMD ["java","-jar","Server.jar"]

from postgres as db
COPY Database/createTables.sql /docker-entrypoint-initdb.d/
COPY Database/createFunctions.sql /docker-entrypoint-initdb.d/
COPY Database/createTriggers.sql /docker-entrypoint-initdb.d/