Product Requirements Document (PRD): Advanced Hotel Management System
1. Project Overview
This project is a standalone desktop application for managing hotel operations, including room inventory, customer reservations, and billing. The system features a Graphical User Interface (GUI) built with JavaFX . It uses JDBC for primary permanent data storage to fulfill advanced evaluation rubrics. The architecture systematically integrates all theoretical concepts from Labs 1 through 9 to demonstrate complete mastery .

2. Technical Stack & Tooling
Language: Java 17+
GUI Framework: JavaFX


UI Design Tool: Scene Builder utilizing .fxml files
Build Automation: Maven or Gradle
Database: Relational Database via JDBC (MySQL or SQLite)
3. Core Features & Architectural Requirements
OOP Concepts (Week 1)
Encapsulation: Model classes must use private fields with public getters and setters .


Inheritance & Polymorphism: Create an abstract base class Room and concrete derived classes like StandardRoom and LuxuryRoom .


Abstraction: Implement an interface Amenities declaring methods like provideWifi() for derived classes to use .


Wrapper Classes & Enumerations (Week 2)
Enums: Define an enum RoomType (e.g., STANDARD, DELUXE, SUITE) initialized with base tariff values via an enum constructor .


Wrappers: Utilize wrapper classes like Integer and Double instead of primitives for financial calculations and collections.


Multithreading & Synchronization (Weeks 3 & 4)
Background Tasks: Implement a live background thread using the Runnable interface to display a real-time clock on the dashboard.


Synchronization: Create a synchronized booking method to ensure two concurrent threads cannot book the same room simultaneously .


I/O Streams, RandomAccessFile & Serialization (Weeks 5 & 6)
I/O Streams: Generate a text-based invoice upon customer checkout using FileWriter or FileOutputStream.


Serialization: Serialize application configurations (like tax rates) to a .dat file using ObjectOutputStream .


RandomAccessFile: Maintain a binary system_events.log that appends critical application actions using RandomAccessFile .


Generics (Week 7)
Generic Classes: Implement a generic Pair<T, U> class to temporarily bind a Customer ID to a Room ID in memory before executing the database commit .


Collections Framework (Week 8)
Data Structures: Use ArrayList to populate the JavaFX TableView components dynamically.


Mapping: Use a HashMap for quick lookups of active user sessions or loaded room data.


Sorting: Use Collections.sort() to order the displayed rooms by price.


JavaFX GUI & Layouts (Weeks 9, 10 & Rubric)
Layout Variety: Utilize a BorderPane for main navigation, GridPane for data entry forms, and VBox/HBox for button alignment.
Components: Integrate TableView, ComboBox, TextField, and Button controls .


Event Handling: Implement clear UI event handling with proper alert dialogs for success and error states .


Billing Management (Rubric Feature)
Checkout Logic: Calculate total days stayed, fetch the base tariff, add standard service charges, and display an itemized bill before executing the database update.
4. Implementation Directives for the AI Assistant
Phase 1: Project Setup: Generate the pom.xml or build.gradle establishing JavaFX and JDBC dependencies, and define the standard project directory structure.
Phase 2: Core Logic (Weeks 1-8): Draft the Java models, enums, threading classes, file I/O utilities, and generic helper classes.
Phase 3: FXML Layouts: Generate the code for the diverse .fxml files representing the Dashboard, Room Management, and Billing screens using Scene Builder conventions.
Phase 4: Controllers & Integration: Create the Java controller classes linking the FXML UI components to the backend logic.
Phase 5: Database Schema: Provide the SQL scripts to create the necessary tables and finalize the JDBC integration.

