## eSakwa - Mobile App
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Examples of functionalities](#examples-of-functionalities)

## General info
Mobile application for expenses management in group of people. 
	
## Technologies
Project is created with:
* Android Studio: 2020.3.1
* Java: 11
* Gradle Version: 6.7.1
* Android Gradle Plugin Version: 4.2.2
	
## Setup
Download or clone this project from https://github.com/kingachwaleba/expense-management-system. 

To store pictures, create a new directory named ***uploads*** in the ***backend*** directory.

To run backend application use:
```
$ cd expense-management-system/backend
$ mvn spring-boot:run
```
Before running project change ip for server app ip in file:
* expense-management-system/mobile/app/src/main/res/xml/network_security_config.xml
* expense-management-system/mobile/app/src/main/java/com/example/mobile/config/ApiClient.java 
* expense-management-system/mobile/app/src/main/java/com/example/mobile/ImageHelper.java

To run mobile app 
* use emulator in Android Studio 
<!-- * or install expense-management-system/mobile/app/build/outputs/apk/debug/app-debug.apk file on your phone -->

## Examples of functionalities
Main function of the application is to simplify debts between members in a group (wallet). App user can do things like:
* register, login, remind password

<p align="center">
	<img src="https://user-images.githubusercontent.com/79993500/149238519-ffe754c8-6957-4c70-a7c3-9a8312778d6e.png" alt="sign_up" width="200" height="430">
	<img src="https://user-images.githubusercontent.com/79993500/149238538-d58dbe41-89d8-426c-95b6-c27b1be1306c.png" alt="sign_in" width="200" height="430">
	<img src="https://user-images.githubusercontent.com/79993500/149238524-400acb61-0fee-4bb3-928b-24f69f2a1a5e.png" alt="remind_password" width="200" height="430">
</p>

* profile management: change photo, change password, delete account, display notification about debts, wallet invitation and new message

<p align="center">
	<img src="https://user-images.githubusercontent.com/79993500/149236400-58b99225-9240-470f-a3d9-8477536af30d.png" alt="profile" width="200" height="430">
	<img src="https://user-images.githubusercontent.com/79993500/149238146-95067fb7-b1e2-4420-a42b-8f3300e13363.png" alt="edit_profile" width="200" height="430">
</p>

* wallet management (the wallet is a group of people who wants to share expenses with each other): create, edit, delete wallet

<p align="center">
	<img src="https://user-images.githubusercontent.com/79993500/149240988-5a66944e-243c-4618-bea5-ceb030acc7ac.png" alt="wallets_list" width="200" height="430">
	<img src="https://user-images.githubusercontent.com/79993500/149240996-ab675896-9259-4272-bcef-7385d802e1c4.png" alt="wallet" width="200" height="430">
</p>

* expense management: create, edit, delete expense in wallet

<p align="center">
	<img src="https://user-images.githubusercontent.com/79993500/149240397-631eabf4-765e-4d7a-af42-7fb381def8ed.png" alt="expenses_list" width="200" height="430">
	<img src="https://user-images.githubusercontent.com/79993500/149240611-1f2ebe28-cd3a-46b4-8b3d-35c51e546890.png" alt="expense" width="200" height="430">
</p>

* display your credits and debts, send remaider to your debtor, check credits as regulated 

<p align="center">
	<img src="https://user-images.githubusercontent.com/79993500/149239886-007ab284-0479-4482-93da-d960b6e4f66d.png" alt="debetor" width="200" height="430">
	<img src="https://user-images.githubusercontent.com/79993500/149239903-c2ef79eb-9fc6-4c57-8fb3-139aa4cea0db.png" alt="creditor" width="200" height="430">
</p>

* display wallet statistics 

<p align="center">
	<img src="https://user-images.githubusercontent.com/79993500/149238811-1b81741a-efb6-4e87-b23a-c6844aa7adcc.png" alt="wallet_statistics" width="200" height="430">
</p>

* shop list management: create, edit, use 

<p align="center">
	<img src="https://user-images.githubusercontent.com/79993500/149239465-fbddcc2e-e264-429f-aeb2-a8271bbceae1.png" alt="lists_list" width="200" height="430">
	<img src="https://user-images.githubusercontent.com/79993500/149239601-145260a9-f422-42bf-a37c-ceaa02114b30.png" alt="list" width="200" height="430">
</p>

* chat 

<p align="center">
	<img src="https://user-images.githubusercontent.com/79993500/149239149-bc380582-258d-4e2d-b96d-97373822d3d9.png" alt="chat" width="200" height="430">
</p>
