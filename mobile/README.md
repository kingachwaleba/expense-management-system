## eSawka - Mobile App
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
Mobile application for expenses managment in group of people. 
	
## Technologies
Project is created with:
* Android Studio: 2020.3.1
* Java: 11
* Gradle Version: 6.7.1
* Andorid Gradle Plugin Version: 4.2.2
	
## Setup
To run this project,
* firstly, download server app from expense-managment-system/backend. To run backend use:
```
$ cd expense-managment-system/backend
$ mvn spring-boot:run
```
Before running project change ip for server app ip in file:
* expense-managment-system/mobile/app/src/main/res/xml/network_security_config.xml
* expense-managment-system/mobile/app/src/main/java/com/example/mobile/config/ApiClient.java 
* expense-managment-system/mobile/app/src/main/java/com/example/mobile/ImageHelper.java

To run mobile app 
* use emulator in Android Studio 
<!-- * or install expense-managment-system/mobile/app/build/outputs/apk/debug/app-debug.apk file on your phone -->
