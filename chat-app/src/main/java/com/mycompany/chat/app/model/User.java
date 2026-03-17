package com.mycompany.chat.app.model;


/**
 * Represents a registered user in the ChatApp system.
 * Designed to be extended as the chat application grows
 * (e.g., adding contacts, message history, online status).
 */
public class User {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

   

    public User(String firstName, String lastName, String username,
                String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', name='" + firstName + " " + lastName + "'}";
    }
}
