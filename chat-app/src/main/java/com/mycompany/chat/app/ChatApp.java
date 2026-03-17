package com.mycompany.chat.app;

import java.util.Scanner;
import com.mycompany.chat.app.Service.Login;
public class ChatApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        String choice = "";
 
        System.out.println("=== Welcome to ChatApp ===");
 
        while (!choice.equals("3")) {
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
 
                String result = login.registerUser(firstName, lastName, username, password, cellPhone);
                System.out.println("\n" + result);
 
            } else if (choice.equals("2")) {
 
                System.out.println("\n--- Login ---");
 
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
 
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
 
                System.out.println("\n" + login.returnLoginStatus(username, password));
 
            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
 
        scanner.close();
    
    }
}
