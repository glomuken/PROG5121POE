/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat.ServiceTest;


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
    private Message message1;
    private Message message2;
 
    @BeforeEach
    void setUp() {
        messageService = new MessageClass();
 
        // Test Data Message 1
        // Recipient: +27718693002
        // Message: "Hi Mike, can you join us for dinner tonight?"
        // Force known ID so hash is predictable
        message1 = new Message("0000000000", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?", "");
        message1.setMessageHash(messageService.createMessageHash("0000000000", 0, message1.getMessageText()));
 
        // Test Data Message 2
        // Recipient: 08575975889 (no international code - invalid)
        // Message: "Hi Keegan, did you receive the payment?"
        message2 = new Message("0000000001", 1, "08575975889", "Hi Keegan, did you receive the payment?", "");
        message2.setMessageHash(messageService.createMessageHash("0000000001", 1, message2.getMessageText()));
    }
 
    // =========================================================================
    // assertEquals TESTS
    // =========================================================================
 
    @Test
    void testMessageLengthSuccess() {
        assertEquals(MESSAGE_READY, messageService.checkMessageLength(message1.getMessageText()));
    }
 
    @Test
    void testMessageLengthFailure() {
        String longMessage = "A".repeat(255);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.", messageService.checkMessageLength(longMessage));
    }
 
    @Test
    void testRecipientCellSuccess() {
        assertEquals(RECIPIENT_SUCCESS, messageService.checkRecipientCell(message1.getRecipientCell()));
    }
 
    @Test
    void testRecipientCellFailure() {
        assertEquals(RECIPIENT_FAILURE, messageService.checkRecipientCell(message2.getRecipientCell()));
    }
 
    @Test
    void testMessageHashCorrect() {
        // ID "0000000000", number 0, message "Hi Mike, can you join us for dinner tonight?"
        // First 2 of ID = "00", message number = 0, first word = "Hi", last word = "tonight?"
        // Expected: 00:0:HITONIGHT?
        assertEquals("00:0:HITONIGHT?", message1.getMessageHash());
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
        assertEquals("Message successfully sent.", messageService.sentMessage(message1, 1));
    }
 
    @Test
    void testSentMessageDisregard() {
        assertEquals("Press 0 to delete the message.", messageService.sentMessage(message1, 0));
    }
 
    @Test
    void testSentMessageStore() {
        assertEquals("Message successfully stored.", messageService.sentMessage(message1, 2));
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
        messageService.sentMessage(message1, 1);
        messageService.sentMessage(message2, 1);
        assertEquals(2, messageService.returnTotalMessages());
    }

    @Test
    void testPrintMessagesOrder() {
        messageService.sentMessage(message1, 1);
        String output = messageService.printMessages();

        assertTrue(output.contains("Message ID: " + message1.getMessageID()));
        assertTrue(output.contains("Message Hash: " + message1.getMessageHash()));
        assertTrue(output.contains("Recipient: " + message1.getRecipientCell()));
        assertTrue(output.contains("Message: " + message1.getMessageText()));
    }
 
    @Test
    void testMessageHashesInLoop() {
        // Test all message hashes are correctly formatted (contain 2 colons)
        String[] messages = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?"
        };
 
        for (int i = 0; i < messages.length; i++) {
            String hash = messageService.createMessageHash("0000000000", i, messages[i]);
            assertTrue(hash.contains(":"), "Hash should contain colons: " + hash);
            assertEquals(hash, hash.toUpperCase(), "Hash should be all caps");
        }
    }
}
