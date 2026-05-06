/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat.app.Service;

import com.mycompany.chat.app.model.Message;
import java.util.ArrayList;
import java.util.Random;

/**
 * MessageService class - handles all message logic for ChatApp.
 */
public class MessageClass {
    private static final String RECIPIENT_SUCCESS = "Cell phone number successfully captured.";
    private static final String RECIPIENT_ERROR = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    private static final String MESSAGE_READY = "Message ready to send.";
    private static final String MESSAGE_TOO_LONG_PREFIX = "Message exceeds 250 characters by ";
    private static final String MESSAGE_TOO_LONG_SUFFIX = "; please reduce the size.";

    // List to store all sent messages during the session
    private ArrayList<Message> sentMessages = new ArrayList<>();
 
    // Tracks how many messages have been sent
    private int totalMessagesSent = 0;
 
    // Checks that the message ID is not more than 10 characters
    public boolean checkMessageID(String messageID) {
        if (messageID == null) {
            return false;
        }
        
        if (messageID.length() <= 10) {
            return true;
        } else {
            return false;
        }
    }
 
    // Checks that the recipient cell number has an international code and correct format
    // Same from Part 1 (Login.checkCellPhoneNumber)
    public String checkRecipientCell(String recipientCell) {
        if (recipientCell == null) {
            return RECIPIENT_ERROR;
        }

        if (recipientCell.length() != 12) {
            return RECIPIENT_ERROR;
        }

        if (recipientCell.charAt(0) != '+' || recipientCell.charAt(1) != '2' || recipientCell.charAt(2) != '7') {
            return RECIPIENT_ERROR;
        }

        for (int i = 3; i < recipientCell.length(); i++) {
            if (!Character.isDigit(recipientCell.charAt(i))) {
            return RECIPIENT_ERROR;
            }
        }

        return RECIPIENT_SUCCESS;
        
    }
 
    // Checks that the message is not more than 250 characters
    public String checkMessageLength(String messageText) {
        if (messageText == null) {
            return "Please enter a message of less than 250 characters.";
        }
        if (messageText.length() <= 250) {
            return MESSAGE_READY;
        } else {
            int over = messageText.length() - 250;
            return MESSAGE_TOO_LONG_PREFIX + over + MESSAGE_TOO_LONG_SUFFIX;
        }
    }
 
 
    // Create the message hash:
    // first 2 digits of ID + ":" + message number + ":" + first word + last word (all caps)
    // Example: 00:0:HITONIGHT?
    public String createMessageHash(String messageID, int messageNumber, String messageText) {
        if (messageID == null || messageID.length() < 2 || messageText == null || messageText.trim().isEmpty()) {
            return "";
        }
 
        String firstTwoID = messageID.substring(0, 2);
 
        // Trim extra spaces and split by one-or-more whitespace characters.
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
 
        String hash = firstTwoID + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }
 
   
    // Builds and returns a new Message object with auto-generated ID and hash
    public Message createMessage(String recipientCell, String messageText) {
        String messageID = generateMessageID();
        int messageNumber = sentMessages.size();
        String messageHash = createMessageHash(messageID, messageNumber, messageText);
        return new Message(messageID, messageNumber, recipientCell, messageText, messageHash);
    }
 
   
    
    // Handles what happens to the message based on user choice
    // 1 = Send, 0 = Disregard, 2 = Store
    public String sentMessage(Message message, int choice) {
        if (message == null) {
            return "Invalid option.";
        }

        if (choice == 1) {
            message.setStatus("Sent");
            sentMessages.add(message);
            totalMessagesSent++;
            return "Message successfully sent.";
        } else if (choice == 0) {
            message.setStatus("Disregarded");
            return "Press 0 to delete the message.";
        } else if (choice == 2) {
            message.setStatus("Stored");
            storeMessage(message);
            return "Message successfully stored.";
        } else {
            return "Invalid option.";
        }
    }
 
    // Stores the message as JSON in a file
    public void storeMessage(Message message) {
        if (message == null) {
            return;
        }
        try {
            String json = "{\n" +
                    "  \"messageID\": \"" + message.getMessageID() + "\",\n" +
                    "  \"messageNumber\": " + message.getMessageNumber() + ",\n" +
                    "  \"recipientCell\": \"" + message.getRecipientCell() + "\",\n" +
                    "  \"message\": \"" + message.getMessageText() + "\",\n" +
                    "  \"messageHash\": \"" + message.getMessageHash() + "\",\n" +
                    "  \"status\": \"Stored\"\n" +
                    "}";
 
            java.io.FileWriter writer = new java.io.FileWriter("stored_messages.json", true);
            writer.write(json + System.lineSeparator());
            writer.close();
 
        } catch (java.io.IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
 
    
 
    // Returns all sent messages formatted as: Message ID, Message Hash, Recipient, Message
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
 
        String output = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            Message m = sentMessages.get(i);
            output += "Message ID: " + m.getMessageID() + "\n";
            output += "Message Hash: " + m.getMessageHash() + "\n";
            output += "Recipient: " + m.getRecipientCell() + "\n";
            output += "Message: " + m.getMessageText() + "\n";
            output += "----------------------------\n";
        }
        return output;
    }
 
    // Returns the total number of messages sent
    public int returnTotalMessages() {
        return totalMessagesSent;
    }
 
 
    // Generates a random 10-digit message ID
    private String generateMessageID() {
        Random random = new Random();
        long id = (long)(random.nextDouble() * 9000000000L) + 1000000000L;
        return String.valueOf(id);
    }
 
    // Returns the sent messages list
    public ArrayList<Message> getSentMessages() {
        return sentMessages;
    }
    
}
