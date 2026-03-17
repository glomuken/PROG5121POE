package com.mycompany.chat.app.Service;

import java.util.ArrayList;
import java.util.List;
import com.mycompany.chat.app.model.User;

public class Login {

    //Return messages
    private static final String USERNAME_SUCCESS_MESSAGE = "Username successfully captured.";
    private static final String USERNAME_ERROR_MESSAGE = "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
    private static final String PASSWORD_SUCCESS_MESSAGE = "Password successfully captured.";
    private static final String PASSWORD_ERROR_MESSAGE = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    private static final String CELL_SUCCESS_MESSAGE = "Cell number successfully captured.";
    private static final String CELL_ERROR_MESSAGE = "Cell number is incorrectly formatted or does not contain an international code.";
    private static final String LOGIN_ERROR_MESSAGE = "Username or password incorrect please try again.";

    // Store registered users in memory for this console app.
    private final List<User> users = new ArrayList<>();

    // check username
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }

        return username.contains("_") && username.length() <= 5;
    }

    // check password
    public boolean checkPasswordComplexity(String password) {
        if (password == null) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }

        boolean capital = false;
        boolean number = false;
        boolean special_char = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                capital = true;
            }
            if (Character.isDigit(c)) {
                number = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                special_char = true;
            }
        }

        return capital && number && special_char;
    }

    // check cell phone
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        // Reference: ITU-T E.164 international format, adapted to SA numbers (+27 + 9 digits).
        return cellPhoneNumber.matches("^\\+27\\d{9}$");
    }

    // register user
    public String registerUser(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return USERNAME_ERROR_MESSAGE;
        }
        if (!checkPasswordComplexity(password)) {
            return PASSWORD_ERROR_MESSAGE;
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return CELL_ERROR_MESSAGE;
        }

        User newUser = new User(firstName, lastName, username, password, cellPhoneNumber);
        users.add(newUser);

        return USERNAME_SUCCESS_MESSAGE + " " + PASSWORD_SUCCESS_MESSAGE + " " + CELL_SUCCESS_MESSAGE;
    }

    // login user
    public boolean loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // login status
    public String returnLoginStatus(String username, String password) {
        if (!loginUser(username, password)) {
            return LOGIN_ERROR_MESSAGE;
        }

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return "Welcome " + user.getFirstName() + "," + user.getLastName() + " it is great to see you again.";
            }
        }

        return LOGIN_ERROR_MESSAGE;
    }

    // get users
    public List<User> getUsers() {
        return users;
    }
}