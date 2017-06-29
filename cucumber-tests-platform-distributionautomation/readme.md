Distribution automation regression tests (Web service)

This readme describes how to run the regression test for DA webservices only.

## prerequisites
- docker (Installed and running)
- java 8
- maven

# How to run
In the main directory (Integration-Tests) run:
mvn -f pom-da.xml clean verify -DskipITs=false

# Other

### Only build and install the neccessary libraries
In the main directory (Integration-Tests) run:
mvn -f pom-da.xml clean install

### Start Postgresql and Activemq
In the cucumber DA directory run:
mvn docker:start -DskipITs=false

This will start an empty database, and a clean activemq.

### Stop Postgresql and Activemq
In the cucumber DA directory run:
mvn docker:stop -DskipITs=false

### Serve DA platform
In the cucumber DA directory run:
mvn cargo:run -DskipITs=false

### Seed the database with demo/cucumber data
In the cucumber DA directory run:
mvn flyway:migrate -DskipITs=false