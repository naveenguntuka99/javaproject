
prerequisites
  Java21
  Maven 3.9+
  Spring boot 3+

Maven will automatically download all required dependencies, if required any depedency we add into pom.xml from mvn repo. 
To manually download dependencies and build the project:
mvn clean install  --Build the project
mvn test  -- To Run Test cases 
mvn spring-boot:run  --application will run on 9090 port


Functionality of the api
  Supported event types: CREDIT and DEBIT.
  Duplicate event IDs are treated as idempotent requests and return the existing event.
  Debit transactions are rejected when account balance is insufficient.
  Events are returned in ascending order of event timestamp.
  Net balance will return from all event transactions 


