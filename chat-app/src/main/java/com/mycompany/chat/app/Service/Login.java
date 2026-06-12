package com.mycompany.chat.app.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private static final String USERNAME_EXISTS_MESSAGE = "Username already exists. Please choose a different username.";
    private static final String USERS_FILE = "users.json";
    private static final String REGISTRATION_SUCCESS_MESSAGE = USERNAME_SUCCESS_MESSAGE + " " + PASSWORD_SUCCESS_MESSAGE + " " + CELL_SUCCESS_MESSAGE;

    // Store registered users in memory for this console app.
    private final List<User> users = new ArrayList<>();
    private boolean usersLoaded = false;

    public Login() {
        loadUsersFromJsonIfNeeded();
    }

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
                continue;
            }
            if (Character.isDigit(c)) {
                number = true;
                continue;
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

        if (cellPhoneNumber.length() != 12) {
            return false;
        }

        if (cellPhoneNumber.charAt(0) != '+' || cellPhoneNumber.charAt(1) != '2' || cellPhoneNumber.charAt(2) != '7') {
            return false;
        }

        for (int i = 3; i < cellPhoneNumber.length(); i++) {
            if (!Character.isDigit(cellPhoneNumber.charAt(i))) {
                return false;
            }
        }

        return true;
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
        if (usernameExists(username)) {
            return USERNAME_EXISTS_MESSAGE;
        }

        User newUser = new User(firstName, lastName, username, password, cellPhoneNumber);
        users.add(newUser);

        return REGISTRATION_SUCCESS_MESSAGE;
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

    public void saveCurrentUser() {
        if (users.isEmpty()) {
            System.out.println("No user to save.");
            return;
        }
        saveUsersToJson();
    }

    public boolean isRegistrationSuccessful(String result) {
        return REGISTRATION_SUCCESS_MESSAGE.equals(result);
    }

    private synchronized void loadUsersFromJsonIfNeeded() {
        if (usersLoaded) {
            return;
        }

        Path path = Path.of(USERS_FILE);
        if (!Files.exists(path)) {
            usersLoaded = true;
            return;
        }

        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            if (content.trim().isEmpty()) {
                usersLoaded = true;
                return;
            }

            Pattern objectPattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
            Matcher objectMatcher = objectPattern.matcher(content);
            while (objectMatcher.find()) {
                String objectBody = objectMatcher.group(1);
                String firstName = extractStringField(objectBody, "firstName");
                String lastName = extractStringField(objectBody, "lastName");
                String username = extractStringField(objectBody, "username");
                String password = extractStringField(objectBody, "password");
                String cellPhoneNumber = extractStringField(objectBody, "cellPhoneNumber");

                if (username.isEmpty()) {
                    continue;
                }

                users.add(new User(firstName, lastName, username, password, cellPhoneNumber));
            }
            usersLoaded = true;
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
            usersLoaded = true;
        }
    }

    private void saveUsersToJson() {
        StringBuilder json = new StringBuilder("[").append(System.lineSeparator());
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            json.append("  {").append(System.lineSeparator());
            json.append("    \"firstName\": \"").append(escapeJson(user.getFirstName())).append("\",").append(System.lineSeparator());
            json.append("    \"lastName\": \"").append(escapeJson(user.getLastName())).append("\",").append(System.lineSeparator());
            json.append("    \"username\": \"").append(escapeJson(user.getUsername())).append("\",").append(System.lineSeparator());
            json.append("    \"password\": \"").append(escapeJson(user.getPassword())).append("\",").append(System.lineSeparator());
            json.append("    \"cellPhoneNumber\": \"").append(escapeJson(user.getCellPhoneNumber())).append("\"").append(System.lineSeparator());
            json.append("  }");
            if (i < users.size() - 1) {
                json.append(",");
            }
            json.append(System.lineSeparator());
        }
        json.append("]").append(System.lineSeparator());

        try {
            Files.writeString(Path.of(USERS_FILE), json.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error storing users: " + e.getMessage());
        }
    }

    private boolean usernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private String extractStringField(String objectBody, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(objectBody);
        if (matcher.find()) {
            return matcher.group(1).replace("\\\\\"", "\\\"");
        }
        return "";
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}