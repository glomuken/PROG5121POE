# PROG5121POE
Kamwanya Mukendi's Repo for Prog 1 POE

## Project Overview

This repository contains my PROG5121 POE work. The code currently in the repository is **Part 1**, which is a simple Java console-based chat application focused on user registration and login. It allows a user to enter their details, checks whether the username, password and cell phone number are correctly formatted, stores registered users in memory, and then allows those users to log in.

Parts **2** and **3** will also be added to this repository as the project grows.

## Project Parts

### Part 1
This part contains the current working code for registration, login, validation, and unit testing.

### Part 2
This section will be added to the repository later.

### Part 3
This section will be added to the repository later.

## What the Code Does

The application starts in `ChatApp.java`, where a menu is shown to the user. The user can choose to register, log in, or exit the program.

During registration, the program asks the user for:
- first name
- last name
- username
- password
- cell phone number

These values are sent to the `Login` service class. The `Login` class checks that:
- the username contains an underscore and is no longer than 5 characters
- the password has at least 8 characters, one capital letter, one number and one special character
- the cell phone number starts with `+27` and is the correct length

If all validations pass, a `User` object is created and added to a list of registered users. If the details are incorrect, the program returns an error message explaining what is wrong.

When the user selects login, the program checks whether the entered username and password match a registered user. If they do, the user receives a welcome message. If not, the program displays an error message.

## File Structure

The main files and folders in this repository are:

```text
PROG5121POE/
├── README.md
└── chat-app/
	├── pom.xml
	├── src/
	│   ├── main/
	│   │   └── java/com/mycompany/chat/app/
	│   │       ├── ChatApp.java
	│   │       ├── Service/
	│   │       │   ├── Login.java
	│   │       │   └── Register.java
	│   │       └── model/
	│   │           └── User.java
	│   └── test/
	│       └── java/com/mycompany/chat/ServiceTest/
	│           └── LoginTest.java
	└── target/
```

### Structure Explanation
- `README.md` - explains the project.
- `chat-app/pom.xml` - Maven configuration file for building and testing the project.
- `ChatApp.java` - the main entry point of the application.
- `Login.java` - contains validation, registration, and login logic.
- `Register.java` - placeholder service class.
- `User.java` - stores user information in an object.
- `LoginTest.java` - contains JUnit tests for the login and validation features.
- `target/` - generated build output created by Maven.

## Programming Concepts Used

### 1. Classes and Objects
This project uses classes such as `ChatApp`, `Login`, and `User`. A class is a blueprint, and an object is an instance created from that blueprint. For example, a `User` object stores information about one registered person.

### 2. Encapsulation
The `User` class stores data in private fields such as `firstName`, `lastName`, `username`, `password`, and `cellPhoneNumber`. The program uses getters and setters to control access to that data.

### 3. Methods
Methods are used to break the program into smaller tasks. For example, `checkUserName()` checks username rules, `checkPasswordComplexity()` checks password rules, and `loginUser()` checks if login details are correct.

### 4. Conditional Statements
The code uses `if`, `else if`, and `else` statements to make decisions. For example, the menu in `ChatApp` decides whether the user wants to register, log in, or exit.

### 5. Loops
The program uses a `while` loop in `ChatApp` to keep showing the menu until the user chooses to exit. It also uses `for` loops in the `Login` class to check password characters and to search through registered users.

### 6. Lists and Data Storage
The program uses `List<User>` with an `ArrayList` to store users while the application is running. This is a simple way to keep user data in memory without using a database.

### 7. Validation
Validation is an important concept in this project. Before a user is registered, the program checks that the data entered is in the correct format. This helps prevent invalid input.

### 8. Input and Output
The program uses `Scanner` to read input from the keyboard and `System.out.println()` to display messages to the user. This is how the console application interacts with the user.

### 9. Testing
The project also includes JUnit tests in `LoginTest.java`. These tests check whether the registration and login methods work as expected, and whether valid and invalid inputs produce the correct results.

## Part 1 Summary

Overall, this code represents **Part 1** of the project. It demonstrates the basics of object-oriented programming in Java and focuses on the core features of user registration and login. It shows how classes, objects, methods, loops, conditionals, validation, and testing can work together to build a simple user registration and login system.

The later parts of the project, **Part 2** and **Part 3**, will be added to this same repository as the application develops further.

## Testing Overview

Part 1 also includes unit tests in `LoginTest.java`. These tests are used to check that the validation and login features behave correctly.

The tests check:
- whether usernames are accepted or rejected correctly
- whether passwords meet the required complexity rules
- whether cell phone numbers are in the correct format
- whether login succeeds when the correct details are used
- whether login fails when incorrect details are entered

This helps show that the main features of Part 1 are working as expected.

### Demo Video
A demo video of the application in action can be found here: [Demo Video](https://youtu.be/RO2yKpM7bcU) 

## References

Apache Maven Project (n.d.) *What is Maven?* Available at: https://maven.apache.org/what-is-maven.html (Accessed: 13 April 2026).

JUnit Team (n.d.) *JUnit 5 User Guide*. Available at: https://junit.org/junit5/docs/current/user-guide/ (Accessed: 13 April 2026).

Oracle (n.d.) *Java Documentation*. Available at: https://docs.oracle.com/en/java/ (Accessed: 13 April 2026).

