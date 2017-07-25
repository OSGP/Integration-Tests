Distribution automation regression tests (Web service)

This readme describes how to run the regression test for DA webservices only.

## prerequisites
- docker (Installed and running)
- java 8
- maven
- OSGP artifacts (Shared/Platform/etc)

# How to run
In the main directory (Integration-Tests) run:
mvn -f pom.xml clean verify -DskipITs=false -DskipServing=false --pl 'cucumber-tests-core,cucumber-tests-platform,cucumber-tests-platform-distributionautomation'

# Other

### Only build and install ALL neccessary libraries + more
In the main directory (Integration-Tests) run:
mvn -f pom.xml clean install

### Start Postgresql and Activemq
In the cucumber DA directory run:
mvn docker:start -DskipITs=false -DskipServing=false

This will start an empty database, and a clean activemq.

### Stop Postgresql and Activemq
In the cucumber DA directory run:
mvn docker:stop -DskipITs=false -DskipServing=false

### Serve DA platform
In the cucumber DA directory run:
mvn cargo:run -DskipITs=false -DskipServing=false

### Seed the database with demo/cucumber data
In the cucumber DA directory run:
mvn flyway:migrate -DskipITs=false -DskipServing=false