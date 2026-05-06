/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat.app.model;
 
/**
 * Represents a single chat message in ChatApp.
 */
public class Message {
 
    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String status;
 
    // Constructor
    public Message(String messageID, int messageNumber, String recipientCell, String messageText, String messageHash) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.messageHash = messageHash;
        this.status = "";
    }
 
    // Getters
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
 
    // Setters
    public void setMessageID(String messageID) { this.messageID = messageID; }
    public void setMessageNumber(int messageNumber) { this.messageNumber = messageNumber; }
    public void setRecipientCell(String recipientCell) { this.recipientCell = recipientCell; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
    public void setMessageHash(String messageHash) { this.messageHash = messageHash; }
    public void setStatus(String status) { this.status = status; }
}
 