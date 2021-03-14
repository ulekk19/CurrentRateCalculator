## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
A simple web app that allows to calculate current GBP/PLN rates.
	
## Technologies
Project is created with:
* Java version: 11
* Vaadin version: 14.4.9
* Spring Boot version: 2.4.3
	
## Setup
To run this project, first you need the following build tools:
* Java JDK (8 or newer) 
* Maven (3 or newer)
* npm (5.6 or newer)

This project can be run either in Java IDE (Eclipse EE, NetBeans IDE, IntelliJ IDEA, VS Code, etc.) which does not require installing any additional tools or it can run it in console. To run this project in console you will need to run following commands:

```
$ mvn compile
$ npm install
$ npm start
$ javac <path_to_java_project_folder>\rate-calculator\src\main\java\upiatek\ratecalculator\RateCalculatorApplication.java
$ java RateCalculatorApplication.class

```

After running RateCalculatorApplication, open up your browser, type and enter in url bar: localhost:8080.
