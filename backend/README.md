## eSakwa - Backend App
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#features)

## General info
Backend application for expenses management in a group of people. 
	
## Technologies
Technologies used to create this project:
* Java: 11
* Maven Version: 3.8.1
* IDE - IntelliJ IDEA Ultimate: 2020.2.3
	
## Setup
1. Download or clone this project from https://github.com/kingachwaleba/expense-management-system.
2. To store pictures, create a new directory named ***uploads*** in the ***backend*** directory.
3. To run backend application use:
```
$ cd expense-management-system/backend
$ mvn spring-boot:run
```

## Features
This application mainly simplifies debts between members of the wallet. A wallet is a group of people who shares expenses with each other. An app user can:
* register, login, remind password,
* profile management: change photo, change password, delete the account, display notifications about debts, wallet invitations, new messages,
* perform CRUD operations on wallets,
* display wallet statistics,
* perform CRUD operations on expenses,
* display credits and debts, send a reminder to the debtor, mark credits as regulated,  
* perform CRUD operations on shop lists,
* use chat. 
