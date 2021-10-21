# CBD_Classes

Create Maven projects: 
$ mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false

Compile the project:
$ mvn package #get dependencies, compiles the project and creates the jar

Execute:
$ mvn exec:java -Dexec.mainClass="weather.WeatherStarter" #adapt to match your own package and class name 