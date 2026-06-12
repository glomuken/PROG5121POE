# PROG5121POE (QuickChat)

This repository contains POE code for:
- **Part 1**: registration and login
- **Part 2**: sending messages
- **Part 3**: storing messages, array reports, and advanced message searches

Registered users are now persisted to `chat-app/users.json` after successful registration and loaded again on app startup.
Users remain as temporary (in-memory/mock) accounts by default; they are only saved persistently to `chat-app/users.json` if the user explicitly chooses to save after registration.

## Part 1-3 Checklist

### Part 1 (Registration and Login)

- Username validation (must include `_` and be <= 5 chars)
- Password complexity validation
- South African format phone validation (`+27...`)
- Login success/fail responses
- Optional user persistence to `chat-app/users.json`

### Part 2 (Messaging Core)

- `Welcome to QuickChat.` displayed after successful login
- Main menu loop runs until user chooses `3. Quit`
- User chooses message count for the session
- Message ID generation (10 digits)
- Recipient validation, message length validation
- Message hash generation using string manipulation
- Send/disregard/store flow implemented
- Sent message details printed in correct order
- Stored messages written to JSON

### Part 3 (Stored Messages and Reports)

The app now includes the required message arrays (populated at runtime, no hard-coded array values):

- Sent Messages
- Disregarded Messages
- Stored Messages (loaded/saved to `stored_messages.json`)
- Message Hashes
- Message IDs

A new main menu option, `4. Stored Messages`, supports:

- Display sender and recipient for all stored messages
- Sender uses the logged-in user's username for new stored messages
- Display the longest stored message
- Search by message ID
- Search all sent/stored messages for a recipient
- Delete a stored message by hash
- Display full stored-messages report

## Part 2 Requirements Received and Checked

The following requirements were captured from your brief/screenshots and checked against the code.

1. Users can only send messages after successful login.
2. After login, show `Welcome to QuickChat.`
3. Show menu:
   - `1. Send Messages`
   - `2. Show recently sent messages` (prints sent message details)
   - `3. Quit`
   - `4. Stored Messages`
4. Program runs in a loop until user chooses quit.
5. User sets how many messages they want to enter at startup.
6. Message rules:
   - Message ID: generated and max 10 chars.
   - Recipient number: international format check (`+27...`) reused from Part 1.
   - Message text: max 250 chars.
   - Message hash format: `firstTwoID:messageNumber:firstWord+lastWord` in uppercase.
7. Send actions:
   - Send -> `Message successfully sent.`
   - Disregard -> `Press 0 to delete the message.`
   - Store -> `Message successfully stored.`
8. Print sent message details in order: ID, Hash, Recipient, Message.
9. Show total number of messages sent.
10. Unit tests for message length, recipient format, hash, message ID creation, send/disregard/store, totals, and looped hash checks.

## What Was Missing and Added

The following updates were implemented to close gaps:

- Enforced message-entry cap in `ChatApp` so users cannot exceed the chosen count in one session (`messagesEntered` + remaining logic).
- Standardized message strings in `MessageClass` with constants to avoid mismatch.
- Hardened hash generation in `MessageClass`:
  - handles short/null IDs safely
  - splits by one-or-more spaces (`\\s+`) to avoid spacing issues
- Improved test quality in `MessageTest`:
  - replaced tautological ID assertion with real ID validity checks
  - added message detail print-order presence checks
- Updated README to include Part 2 requirement checklist and implementation status.

## Current Project Structure

```text
PROG5121POE/
├── README.md
└── chat-app/
	├── pom.xml
	├── src/main/java/com/mycompany/chat/app/
	│   ├── ChatApp.java
	│   ├── Service/
	│   │   ├── Login.java
	│   │   ├── MessageClass.java
	│   │   └── Register.java
	│   └── model/
	│       ├── Message.java
	│       └── User.java
	└── src/test/java/com/mycompany/chat/ServiceTest/
		├── LoginTest.java
		└── MessageTest.java
```

## How to Run

If Maven is installed:

```powershell
cd "C:\Users\Dell\Downloads\PROG5121POE\chat-app"
mvn test
mvn exec:java -Dexec.mainClass="com.mycompany.chat.app.ChatApp"
```

If Maven is not installed, compile manually:

```powershell
cd "C:\Users\Dell\Downloads\PROG5121POE\chat-app\src\main\java"
javac com\mycompany\chat\app\ChatApp.java com\mycompany\chat\app\Service\Login.java com\mycompany\chat\app\Service\MessageClass.java com\mycompany\chat\app\model\User.java com\mycompany\chat\app\model\Message.java
java com.mycompany.chat.app.ChatApp
```

## Test Coverage Summary

`MessageTest` currently checks:
- message length pass/fail
- recipient format pass/fail
- exact hash for sample data (`10:1:DIDCAKE?`)
- valid/invalid message ID
- send/disregard/store return strings
- total sent count
- hash checks in loop
- printed message detail presence
- arrays populated correctly
- longest stored message
- message ID lookup
- recipient-based message search
- delete by hash
- report output fields

`LoginTest` currently checks:
- registration and login success/failure flows
- username/password/cell validations
- duplicate username rejection
- registration success helper used by UI

## References

Apache Maven Project (n.d.) *What is Maven?* Available at: https://maven.apache.org/what-is-maven.html (Accessed: 06 May 2026).

JUnit Team (n.d.) *JUnit 5 User Guide*. Available at: https://junit.org/junit5/docs/current/user-guide/ (Accessed: 06 May 2026).

Oracle (n.d.) *Java Documentation*. Available at: https://docs.oracle.com/en/java/ (Accessed: 06 May 2026).

