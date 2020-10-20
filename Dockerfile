from openjdk as client
WORKDIR /
ADD Jar/Client.jar Client.jar
ADD Jar/configClient.txt configClient.txt
CMD ["java","-jar","Client.jar"]

from openjdk as serveur
WORKDIR /
ADD Jar/Serveur.jar Serveur.jar
ADD Jar/configDB.txt configDB.txt
CMD ["java","-jar","Serveur.jar"]

from postgres as db
COPY Database/20createTables.sql /docker-entrypoint-initdb.d/
COPY Database/30createFunctions.sql /docker-entrypoint-initdb.d/
COPY Database/40createTriggers.sql /docker-entrypoint-initdb.d/


