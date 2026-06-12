/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat.ServiceTest;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mycompany.chat.app.Service.MessageClass;
import com.mycompany.chat.app.model.Message;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    private static final String RECIPIENT_SUCCESS = "Cell phone number successfully captured.";
    private static final String RECIPIENT_FAILURE = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    private static final String MESSAGE_READY = "Message ready to send.";

    private MessageClass messageService;
    private Message testMessage1;
    private Message testMessage2;
    private Message testMessage3;
    private Message testMessage4;
    private Message testMessage5;
 
    @BeforeEach
    void setUp() throws IOException {
        // Keep tests deterministic by clearing persisted stored messages.
        Files.writeString(Path.of("stored_messages.json"), "", StandardCharsets.UTF_8);
        messageService = new MessageClass();

        testMessage1 = buildMessage("1000000001", 1, "+27834557896", "Did you get the cake?", "Developer");
        testMessage2 = buildMessage("1000000002", 2, "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Developer");
        testMessage3 = buildMessage("1000000003", 3, "+27834484567", "Yhoooo, I am at your gate.", "Developer");
        testMessage4 = buildMessage("0838884567", 4, "0838884567", "It is dinner time !", "Developer");
        testMessage5 = buildMessage("1000000005", 5, "+27838884567", "Ok, I am leaving without you.", "Developer");
    }

    private Message buildMessage(String id, int number, String recipient, String text, String sender) {
        String hash = messageService.createMessageHash(id, number, text);
        Message message = new Message(id, number, recipient, text, hash, sender);
        return message;
    }

    private void populateMessagesOneToFour() {
        messageService.sentMessage(testMessage1, 1);
        messageService.sentMessage(testMessage2, 2);
        messageService.sentMessage(testMessage3, 0);
        messageService.sentMessage(testMessage4, 1);
    }

    private void populateAllMessagesOneToFive() {
        populateMessagesOneToFour();
        messageService.sentMessage(testMessage5, 2);
    }
 
    // =========================================================================
    // assertEquals TESTS
    // =========================================================================
 
    @Test
    void testMessageLengthSuccess() {
        assertEquals(MESSAGE_READY, messageService.checkMessageLength(testMessage1.getMessageText()));
    }
 
    @Test
    void testMessageLengthFailure() {
        String longMessage = "A".repeat(255);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.", messageService.checkMessageLength(longMessage));
    }
 
    @Test
    void testRecipientCellSuccess() {
        assertEquals(RECIPIENT_SUCCESS, messageService.checkRecipientCell(testMessage1.getRecipientCell()));
    }
 
    @Test
    void testRecipientCellFailure() {
        assertEquals(RECIPIENT_FAILURE, messageService.checkRecipientCell("08575975889"));
    }
 
    @Test
    void testMessageHashCorrect() {
        assertEquals("10:1:DIDCAKE?", testMessage1.getMessageHash());
    }
 
    @Test
    void testMessageIDCreated() {
        Message newMessage = messageService.createMessage("+27718693002", "Hello there");
        assertNotNull(newMessage.getMessageID());
        assertTrue(messageService.checkMessageID(newMessage.getMessageID()));

        for (int i = 0; i < newMessage.getMessageID().length(); i++) {
            assertTrue(Character.isDigit(newMessage.getMessageID().charAt(i)), "Message ID must contain digits only");
        }
    }
 
    @Test
    void testSentMessageSend() {
        assertEquals("Message successfully sent.", messageService.sentMessage(testMessage1, 1));
    }
 
    @Test
    void testSentMessageDisregard() {
        assertEquals("Press 0 to delete the message.", messageService.sentMessage(testMessage1, 0));
    }
 
    @Test
    void testSentMessageStore() {
        assertEquals("Message successfully stored.", messageService.sentMessage(testMessage1, 2));
    }
 
    // =========================================================================
    // assertTrue / assertFalse TESTS
    // =========================================================================
 
    @Test
    void testCheckMessageIDValid() {
        assertTrue(messageService.checkMessageID("0000000000"));
    }
 
    @Test
    void testCheckMessageIDInvalid() {
        assertFalse(messageService.checkMessageID("12345678901")); // 11 digits
    }
 
    @Test
    void testReturnTotalMessages() {
        messageService.sentMessage(testMessage1, 1);
        messageService.sentMessage(testMessage2, 1);
        assertEquals(2, messageService.returnTotalMessages());
    }

    @Test
    void testPrintMessagesOrder() {
        messageService.sentMessage(testMessage1, 1);
        String output = messageService.printMessages();

        assertTrue(output.contains("Message ID: " + testMessage1.getMessageID()));
        assertTrue(output.contains("Message Hash: " + testMessage1.getMessageHash()));
        assertTrue(output.contains("Recipient: " + testMessage1.getRecipientCell()));
        assertTrue(output.contains("Message: " + testMessage1.getMessageText()));
    }
 
    @Test
    void testMessageHashesInLoop() {
        // Test all message hashes are correctly formatted (contain 2 colons)
        String[] messages = {
            "Did you get the cake?",
            "Where are you? You are late! I have asked you to be on time."
        };
 
        for (int i = 0; i < messages.length; i++) {
            String hash = messageService.createMessageHash("0000000000", i, messages[i]);
            assertTrue(hash.contains(":"), "Hash should contain colons: " + hash);
            assertEquals(hash, hash.toUpperCase(), "Hash should be all caps");
        }
    }

    @Test
    void testSentMessagesArrayCorrectlyPopulated() {
        populateMessagesOneToFour();
        Message[] sent = messageService.getSentMessagesArray();

        assertEquals(2, sent.length);
        assertEquals("Did you get the cake?", sent[0].getMessageText());
        assertEquals("It is dinner time !", sent[1].getMessageText());
    }

    @Test
    void testDisplayLongestMessage() {
        populateMessagesOneToFour();
        assertEquals("Where are you? You are late! I have asked you to be on time.", messageService.getLongestStoredMessage());
    }

    @Test
    void testSearchForMessageID() {
        populateMessagesOneToFour();
        String result = messageService.searchMessageByID("0838884567");

        assertEquals("It is dinner time !", result);
    }

    @Test
    void testSearchMessagesForRecipientSentOrStored() {
        populateAllMessagesOneToFive();
        String result = messageService.searchMessagesForRecipient("+27838884567");

        String expected = "Where are you? You are late! I have asked you to be on time." + System.lineSeparator()
                + "Ok, I am leaving without you.";
        assertEquals(expected, result);
    }

    @Test
    void testDeleteMessageUsingHash() {
        populateAllMessagesOneToFive();
        String response = messageService.deleteMessageByHash(testMessage2.getMessageHash());

        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", response);
    }

    @Test
    void testDisplayReport() {
        populateAllMessagesOneToFive();
        String report = messageService.displayStoredMessagesReport();

        assertTrue(report.contains("Message Hash"));
        assertTrue(report.contains("Recipient"));
        assertTrue(report.contains("Message"));
    }

    @Test
    void testCreateMessageWithSenderUsesLoggedInUser() {
        Message message = messageService.createMessageWithSender("+27838884567", "Hi there", "test_1");
        assertEquals("test_1", message.getSender());
    }
}
