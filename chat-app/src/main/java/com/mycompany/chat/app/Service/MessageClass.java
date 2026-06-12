/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat.app.Service;

import com.mycompany.chat.app.model.Message;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MessageService class - handles all message logic for ChatApp.
 */
public class MessageClass {
    private static final String RECIPIENT_SUCCESS = "Cell phone number successfully captured.";
    private static final String RECIPIENT_ERROR = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    private static final String MESSAGE_READY = "Message ready to send.";
    private static final String MESSAGE_TOO_LONG_PREFIX = "Message exceeds 250 characters by ";
    private static final String MESSAGE_TOO_LONG_SUFFIX = "; please reduce the size.";

    private static final String STORED_MESSAGES_FILE = "stored_messages.json";

    // Part 3 lists required
    private final ArrayList<Message> sentMessages = new ArrayList<>();
    private final ArrayList<Message> disregardedMessages = new ArrayList<>();
    private final ArrayList<Message> storedMessages = new ArrayList<>();
    private final ArrayList<String> messageHashes = new ArrayList<>();
    private final ArrayList<String> messageIDs = new ArrayList<>();
    private String currentUser = "";
 
    private int totalMessagesSent = 0;
    //initialise by getting stored messages on call
    public MessageClass() {
        loadStoredMessagesFromJson();
    }
 
    // Checks that the message ID is not more than 10 characters
    public boolean checkMessageID(String messageID) {
        if (messageID == null) {
            return false;
        }
        
        return messageID.length() <= 10;
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
        int messageNumber = sentMessages.size() + storedMessages.size() + disregardedMessages.size();
        String messageHash = createMessageHash(messageID, messageNumber, messageText);
        String sender = currentUser == null || currentUser.isBlank() ? "Unknown" : currentUser;
        return new Message(messageID, messageNumber, recipientCell, messageText, messageHash, sender);
    }

    public Message createMessageWithSender(String recipientCell, String messageText, String sender) {
        String messageID = generateMessageID();
        int messageNumber = sentMessages.size() + storedMessages.size() + disregardedMessages.size();
        String messageHash = createMessageHash(messageID, messageNumber, messageText);
        String senderValue = sender == null || sender.isBlank() ? "Unknown" : sender;
        return new Message(messageID, messageNumber, recipientCell, messageText, messageHash, senderValue);
    }

    public void setCurrentUser(String username) {
        this.currentUser = username == null ? "" : username;
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
            addToHashAndIdArrays(message);
            totalMessagesSent++;
            return "Message successfully sent.";
        } else if (choice == 0) {
            message.setStatus("Disregarded");
            disregardedMessages.add(message);
            addToHashAndIdArrays(message);
            return "Press 0 to delete the message.";
        } else if (choice == 2) {
            message.setStatus("Stored");
            storedMessages.add(message);
            addToHashAndIdArrays(message);
            saveStoredMessagesToJson();
            return "Message successfully stored.";
        } else {
            return "Invalid option.";
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
        String id;
        do {
            long generated = (long)(random.nextDouble() * 9000000000L) + 1000000000L;
            id = String.valueOf(generated);
        } while (messageIDs.contains(id));
        return id;
    }

    // Returns the sent messages list
    public ArrayList<Message> getSentMessages() {
        return sentMessages;
    }

    public Message[] getSentMessagesArray() {
        return sentMessages.toArray(new Message[0]);
    }

    public Message[] getDisregardedMessagesArray() {
        return disregardedMessages.toArray(new Message[0]);
    }

    public Message[] getStoredMessagesArray() {
        return storedMessages.toArray(new Message[0]);
    }

    public String[] getMessageHashesArray() {
        return messageHashes.toArray(new String[0]);
    }

    public String[] getMessageIDsArray() {
        return messageIDs.toArray(new String[0]);
    }

    public String displayStoredSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        StringBuilder output = new StringBuilder();
        for (Message message : storedMessages) {
            output.append("Sender: ")
                    .append(message.getSender())
                    .append(", Recipient: ")
                    .append(message.getRecipientCell())
                    .append(System.lineSeparator());
        }
        return output.toString().trim();
    }

    public String getLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        Message longest = storedMessages.get(0);
        for (Message message : storedMessages) {
            if (message.getMessageText() != null && message.getMessageText().length() > longest.getMessageText().length()) {
                longest = message;
            }
        }
        return longest.getMessageText();
    }

    public String searchMessageByID(String messageID) {
        Message found = findMessageByID(sentMessages, messageID);
        if (found == null) {
            found = findMessageByID(storedMessages, messageID);
        }
        if (found == null) {
            found = findMessageByID(disregardedMessages, messageID);
        }

        if (found != null) {
            return found.getMessageText();
        }

        return "Message not found.";
    }

    public String searchMessageDetailsByID(String messageID) {
        Message found = findMessageByID(sentMessages, messageID);
        if (found == null) {
            found = findMessageByID(storedMessages, messageID);
        }
        if (found == null) {
            found = findMessageByID(disregardedMessages, messageID);
        }

        if (found != null) {
            return "Recipient: " + found.getRecipientCell() + System.lineSeparator() + "Message: " + found.getMessageText();
        }

        return "Message not found.";
    }

    public String searchMessagesForRecipient(String recipientCell) {
        List<String> results = new ArrayList<>();
        for (Message message : sentMessages) {
            if (message.getRecipientCell().equals(recipientCell)) {
                results.add(message.getMessageText());
            }
        }
        for (Message message : storedMessages) {
            if (message.getRecipientCell().equals(recipientCell)) {
                results.add(message.getMessageText());
            }
        }

        if (results.isEmpty()) {
            return "No messages found for recipient.";
        }
        return String.join(System.lineSeparator(), results);
    }

    public String deleteMessageByHash(String messageHash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            Message message = storedMessages.get(i);
            if (message.getMessageHash().equals(messageHash)) {
                storedMessages.remove(i);
                messageHashes.remove(messageHash);
                messageIDs.remove(message.getMessageID());
                saveStoredMessagesToJson();
                return "Message: \"" + message.getMessageText() + "\" successfully deleted.";
            }
        }
        return "Message not found.";
    }

    public String displayStoredMessagesReport() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        StringBuilder output = new StringBuilder("Stored Messages Report").append(System.lineSeparator());
        for (Message message : storedMessages) {
            output.append("Message ID: ").append(message.getMessageID()).append(System.lineSeparator());
            output.append("Message Hash: ").append(message.getMessageHash()).append(System.lineSeparator());
            output.append("Sender: ").append(message.getSender()).append(System.lineSeparator());
            output.append("Recipient: ").append(message.getRecipientCell()).append(System.lineSeparator());
            output.append("Message: ").append(message.getMessageText()).append(System.lineSeparator());
            output.append("Status: ").append(message.getStatus()).append(System.lineSeparator());
            output.append("----------------------------").append(System.lineSeparator());
        }
        return output.toString().trim();
    }

    private void addToHashAndIdArrays(Message message) {
        if (message != null) {
            messageHashes.add(message.getMessageHash());
            messageIDs.add(message.getMessageID());
        }
    }

    private Message findMessageByID(List<Message> messages, String messageID) {
        for (Message message : messages) {
            if (message.getMessageID().equals(messageID)) {
                return message;
            }
        }
        return null;
    }

    private void loadStoredMessagesFromJson() {
        Path path = Path.of(STORED_MESSAGES_FILE);
        if (!Files.exists(path)) {
            return;
        }

        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            if (content.trim().isEmpty()) {
                return;
            }

            Pattern objectPattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
            Matcher objectMatcher = objectPattern.matcher(content);
            while (objectMatcher.find()) {
                String objectBody = objectMatcher.group(1);
                String messageID = extractStringField(objectBody, "messageID");
                int messageNumber = extractIntField(objectBody, "messageNumber");
                String recipient = extractStringField(objectBody, "recipientCell");
                String messageText = extractStringField(objectBody, "message");
                String messageHash = extractStringField(objectBody, "messageHash");
                String status = extractStringField(objectBody, "status");
                String sender = extractStringField(objectBody, "sender");

                if (messageID.isEmpty() || recipient.isEmpty()) {
                    continue;
                }

                if (sender.isEmpty()) {
                    sender = "Unknown";
                }
                if (status.isEmpty()) {
                    status = "Stored";
                }

                Message message = new Message(messageID, messageNumber, recipient, messageText, messageHash, sender);
                message.setStatus(status);
                storedMessages.add(message);
                addToHashAndIdArrays(message);
            }

        } catch (IOException e) {
            System.out.println("Error reading stored messages: " + e.getMessage());
        }
    }

    private void saveStoredMessagesToJson() {
        StringBuilder json = new StringBuilder("[").append(System.lineSeparator());
        for (int i = 0; i < storedMessages.size(); i++) {
            Message message = storedMessages.get(i);
            json.append("  {").append(System.lineSeparator());
            json.append("    \"messageID\": \"").append(escapeJson(message.getMessageID())).append("\",").append(System.lineSeparator());
            json.append("    \"messageNumber\": ").append(message.getMessageNumber()).append(",").append(System.lineSeparator());
            json.append("    \"recipientCell\": \"").append(escapeJson(message.getRecipientCell())).append("\",").append(System.lineSeparator());
            json.append("    \"message\": \"").append(escapeJson(message.getMessageText())).append("\",").append(System.lineSeparator());
            json.append("    \"messageHash\": \"").append(escapeJson(message.getMessageHash())).append("\",").append(System.lineSeparator());
            json.append("    \"status\": \"").append(escapeJson(message.getStatus())).append("\",").append(System.lineSeparator());
            json.append("    \"sender\": \"").append(escapeJson(message.getSender())).append("\"").append(System.lineSeparator());
            json.append("  }");
            if (i < storedMessages.size() - 1) {
                json.append(",");
            }
            json.append(System.lineSeparator());
        }
        json.append("]").append(System.lineSeparator());

        try {
            Files.writeString(Path.of(STORED_MESSAGES_FILE), json.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    private String extractStringField(String objectBody, String fieldName) {
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(fieldName) + "\\\"\\s*:\\s*\\\"(.*?)\\\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(objectBody);
        if (matcher.find()) {
            return matcher.group(1).replace("\\\\\"", "\\\"");
        }
        return "";
    }

    private int extractIntField(String objectBody, String fieldName) {
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(fieldName) + "\\\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(objectBody);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    
}
