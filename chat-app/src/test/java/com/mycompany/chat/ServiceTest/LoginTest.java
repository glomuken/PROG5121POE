package com.mycompany.chat.ServiceTest;



import com.mycompany.chat.app.Service.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    private static final String USERNAME_ERROR_MESSAGE = "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
    private static final String PASSWORD_SUCCESS_MESSAGE = "Password successfully captured.";
    private static final String PASSWORD_ERROR_MESSAGE = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    private static final String CELL_SUCCESS_MESSAGE = "Cell number successfully captured.";
    private static final String CELL_ERROR_MESSAGE = "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
    private static final String LOGIN_SUCCESS_MESSAGE = "Welcome John,Doe it is great to see you again.";
    private static final String LOGIN_ERROR_MESSAGE = "Username or password incorrect please try again.";
    private static final String USERNAME_EXISTS_MESSAGE = "Username already exists. Please choose a different username.";

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
        // Register a user before each test so login tests have someone to authenticate
        login.registerUser("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    // =========================================================================
    // assertEquals TESTS
    // =========================================================================

    @Test
    void testUsernameCorrectlyFormatted() {
        assertEquals(LOGIN_SUCCESS_MESSAGE, login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testUsernameIncorrectlyFormatted() {
        String result = login.registerUser("John", "Doe", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(USERNAME_ERROR_MESSAGE, result);
    }

    @Test
    void testPasswordMeetsComplexity() {
        String result = login.checkPasswordComplexity("Ch&&sec@ke99!")
                ? PASSWORD_SUCCESS_MESSAGE
                : PASSWORD_ERROR_MESSAGE;
        assertEquals(PASSWORD_SUCCESS_MESSAGE, result);
    }

    @Test
    void testPasswordDoesNotMeetComplexity() {
        String result = login.checkPasswordComplexity("password")
                ? PASSWORD_SUCCESS_MESSAGE
                : PASSWORD_ERROR_MESSAGE;
        assertEquals(PASSWORD_ERROR_MESSAGE, result);
    }

    @Test
    void testCellPhoneCorrectlyFormatted() {
        String result = login.checkCellPhoneNumber("+27838968976")
                ? CELL_SUCCESS_MESSAGE
                : CELL_ERROR_MESSAGE;
        assertEquals(CELL_SUCCESS_MESSAGE, result);
    }

    @Test
    void testCellPhoneIncorrectlyFormatted() {
        String result = login.checkCellPhoneNumber("08966553")
                ? CELL_SUCCESS_MESSAGE
                : CELL_ERROR_MESSAGE;
        assertEquals(CELL_ERROR_MESSAGE, result);
    }

    @Test
    void testReturnLoginStatusWhenLoginFails() {
        assertEquals(LOGIN_ERROR_MESSAGE, login.returnLoginStatus("kyl_1", "wrongpassword"));
    }

    @Test
    void testDuplicateUsernameRejected() {
        String result = login.registerUser("Jane", "Doe", "kyl_1", "Strong@123", "+27839999999");
        assertEquals(USERNAME_EXISTS_MESSAGE, result);
    }

    @Test
    void testIsRegistrationSuccessfulHelper() {
        String result = login.registerUser("Jane", "Doe", "jan_1", "Strong@123", "+27839999999");
        assertTrue(login.isRegistrationSuccessful(result));
    }

    // =========================================================================
    // assertTrue / assertFalse TESTS
    // =========================================================================

    @Test
    void testLoginSuccessful() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testLoginFailed() {
        assertFalse(login.loginUser("kyl_1", "wrongpassword"));
    }

    @Test
    void testUsernameCorrectlyFormattedBool() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void testUsernameIncorrectlyFormattedBool() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    void testPasswordMeetsComplexityBool() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void testPasswordDoesNotMeetComplexityBool() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    void testCellPhoneCorrectlyFormattedBool() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void testCellPhoneIncorrectlyFormattedBool() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}

