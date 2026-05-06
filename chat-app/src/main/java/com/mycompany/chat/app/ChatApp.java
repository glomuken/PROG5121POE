package com.mycompany.chat.app;

import java.util.Scanner;
import com.mycompany.chat.app.Service.Login;
import com.mycompany.chat.app.Service.MessageClass;
import com.mycompany.chat.app.model.Message;
public class ChatApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        String choice = "";
        boolean loggedIn = false;
        MessageClass messageService = new MessageClass();
        

        System.out.println("=== Welcome to ChatApp ===");

        // --- Registration / Login (must login before accessing QuickChat) ---
        while (!loggedIn) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine();
 
            if (choice.equals("1")) {
 
                System.out.println("\n--- Register ---");
 
                System.out.print("Enter first name: ");
                String firstName = scanner.nextLine();
 
                System.out.print("Enter last name: ");
                String lastName = scanner.nextLine();
 
                System.out.print("Enter username (max 5 chars, must include '_'): ");
                String username = scanner.nextLine();
 
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
 
                System.out.print("Enter cell phone number (e.g. +27838968976): ");
                String cellPhone = scanner.nextLine();
 
                System.out.println("\n" + login.registerUser(firstName, lastName, username, password, cellPhone));
 
            } else if (choice.equals("2")) {
 
                System.out.println("\n--- Login ---");
 
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
 
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
 
                System.out.println("\n" + login.returnLoginStatus(username, password));
 
                if (login.loginUser(username, password)) {
                    loggedIn = true;
                }
 
            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
                scanner.close();
                return;
            } else {
                System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
 
        // --- QuickChat Menu (only reached after successful login) ---
        System.out.println("\nWelcome to QuickChat.");
 
        System.out.print("How many messages would you like to send? ");
        int numMessages = Integer.parseInt(scanner.nextLine());
        int messagesEntered = 0;
 
        String chatChoice = "";
 
        while (!chatChoice.equals("3")) {
            System.out.println("\n1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");
            chatChoice = scanner.nextLine();
 
            if (chatChoice.equals("1")) {
                if (messagesEntered >= numMessages) {
                    System.out.println("Message limit reached for this session.");
                    continue;
                }

                int remaining = numMessages - messagesEntered;
                System.out.println("You can enter " + remaining + " more message(s).");
 
                for (int i = 0; i < remaining; i++) {
                    System.out.println("\n--- Message " + (messagesEntered + 1) + " of " + numMessages + " ---");
 
                    System.out.print("Enter recipient cell number (e.g. +27718693002): ");
                    String recipient = scanner.nextLine();
 
                    // Validate recipient before continuing
                    String recipientCheck = messageService.checkRecipientCell(recipient);
                    System.out.println(recipientCheck);
                    if (!recipientCheck.equals("Cell phone number successfully captured.")) {
                        System.out.println("Skipping message.");
                        continue;
                    }
 
                    System.out.print("Enter message: ");
                    String messageText = scanner.nextLine();
 
                    // Validate message length before continuing
                    String lengthCheck = messageService.checkMessageLength(messageText);
                    System.out.println(lengthCheck);
                    if (!lengthCheck.equals("Message ready to send.")) {
                        System.out.println("Skipping message.");
                        continue;
                    }
 
                    // Build the message
                    Message message = messageService.createMessage(recipient, messageText);
                    System.out.println("Message Hash: " + message.getMessageHash());
 
                    // Ask what to do with the message
                    System.out.println("\n1. Send Message");
                    System.out.println("0. Disregard Message");
                    System.out.println("2. Store Message to send later");
                    System.out.print("Choose an option: ");
                    int sendChoice = Integer.parseInt(scanner.nextLine());
 
                    System.out.println(messageService.sentMessage(message, sendChoice));

                    messagesEntered++;
 
                    // Display full details after sending
                    if (sendChoice == 1) {
                        System.out.println("\nMessage ID: " + message.getMessageID());
                        System.out.println("Message Hash: " + message.getMessageHash());
                        System.out.println("Recipient: " + message.getRecipientCell());
                        System.out.println("Message: " + message.getMessageText());
                    }
                }
 
                System.out.println("\nTotal messages sent: " + messageService.returnTotalMessages());
 
            } else if (chatChoice.equals("2")) {
                System.out.println("\nComing Soon.");
 
            } else if (chatChoice.equals("3")) {
                System.out.println("\nTotal messages sent: " + messageService.returnTotalMessages());
                System.out.println("Goodbye!");
 
            } else {
                System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
 
        scanner.close();
    }
}
