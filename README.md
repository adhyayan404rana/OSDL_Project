# 🏨 Advanced Hotel Management System

A full-featured desktop hotel management application built with **Java 17** and **JavaFX**, demonstrating mastery of core Java concepts including OOP, Multithreading, Generics, Collections, and File I/O.

<!-- Uncomment the line below once you add a demo GIF -->
<!-- ![Demo](docs/demo.gif) -->

## ✨ Features

- 🔐 **User Authentication** — Login system with role-based access control
- 🛏️ **Room Management** — Add, edit, and view rooms with real-time status tracking
- 📅 **Reservation System** — Book rooms with date-based availability checking
- 💰 **Billing & Checkout** — Automated invoice generation with itemized bills
- 🧹 **Housekeeping Management** — Track and manage room cleaning status
- 📊 **Reporting & Analytics** — Generate occupancy and revenue reports
- 🕐 **Live Dashboard Clock** — Real-time clock running on a background daemon thread
- 👥 **Guest Management** — Full guest lifecycle tracking

## 🛠️ Tech Stack

| Technology | Purpose |
|-----------|---------|
| Java 17 | Core language |
| JavaFX 21 | GUI framework |
| Maven | Build automation & dependency management |
| Scene Builder | FXML-based UI layout design |
| File I/O | Data persistence (invoices, config, event logs) |

## 📂 Project Structure

```
hotel-management-system-javafx/
├── HotelManagementSystem/
│   ├── pom.xml                          # Maven build configuration
│   └── src/main/
│       ├── java/com/hotel/
│       │   ├── MainApp.java             # Application entry point
│       │   ├── controller/              # JavaFX controllers (MVC)
│       │   │   ├── LoginController.java
│       │   │   ├── DashboardController.java
│       │   │   ├── RoomManagementController.java
│       │   │   ├── ReservationController.java
│       │   │   ├── BillingController.java
│       │   │   ├── HousekeepingController.java
│       │   │   ├── ReportingController.java
│       │   │   ├── GuestManagementController.java
│       │   │   └── MainLayoutController.java
│       │   ├── model/                   # Data models (OOP)
│       │   │   ├── Room.java            # Abstract base class
│       │   │   ├── StandardRoom.java    # Extends Room
│       │   │   ├── LuxuryRoom.java      # Extends Room
│       │   │   ├── ExecutiveRoom.java   # Extends Room
│       │   │   ├── FamilyRoom.java      # Extends Room
│       │   │   ├── Customer.java
│       │   │   ├── Reservation.java
│       │   │   ├── User.java
│       │   │   ├── RoomType.java        # Enum with tariff values
│       │   │   ├── RoomStatus.java      # Enum
│       │   │   ├── UserRole.java        # Enum
│       │   │   └── Amenities.java       # Interface
│       │   ├── service/
│       │   │   └── AuthService.java     # Authentication logic
│       │   └── util/
│       │       ├── FileIOUtility.java   # File I/O, Serialization, RAF
│       │       ├── ClockTask.java       # Background thread (Runnable)
│       │       └── Pair.java            # Generic utility class
│       └── resources/com/hotel/
│           ├── fxml/                    # Scene Builder FXML layouts
│           └── images/rooms/            # Room type images
├── docs/
│   ├── PRD.md                           # Product Requirements Document
│   └── demonstration.md                 # Concept-to-code mapping guide
├── .gitignore
├── LICENSE
└── README.md
```

## 🧠 Java Concepts Demonstrated

| Concept | Where It's Used |
|---------|----------------|
| **OOP — Encapsulation** | Private fields + getters/setters in all model classes |
| **OOP — Inheritance & Polymorphism** | Abstract `Room` → `StandardRoom`, `LuxuryRoom`, `ExecutiveRoom`, `FamilyRoom` |
| **OOP — Abstraction** | `Amenities` interface with contract methods like `provideWifi()` |
| **Enumerations** | `RoomType`, `RoomStatus`, `UserRole` with enum constructors & tariff values |
| **Wrapper Classes** | `Integer`, `Double` used for Collections compatibility & null safety |
| **Multithreading** | `ClockTask.java` — live clock as a daemon `Runnable` thread |
| **Synchronization** | `synchronized` booking methods to prevent race conditions |
| **File I/O (Streams)** | `FileWriter` for invoice generation in `FileIOUtility.java` |
| **Serialization** | `ObjectOutputStream` for app config persistence (`.dat` files) |
| **RandomAccessFile** | Append-only binary event logging in `FileIOUtility.java` |
| **Generics** | Custom `Pair<T, U>` class for type-safe key-value binding |
| **Collections** | `ArrayList` for TableView data, `HashMap` for lookups, `Collections.sort()` |
| **JavaFX GUI** | `BorderPane`, `GridPane`, `VBox`, `TableView`, `ComboBox`, event handling |

## ▶️ How to Run

### Prerequisites

- **Java 17+** — [Download from Adoptium](https://adoptium.net/)
- **Maven** — [Download from Apache](https://maven.apache.org/download.cgi)

### Build & Run

```bash
# Clone the repository
git clone https://github.com/adhyayan404rana/hotel-management-system-javafx.git
cd hotel-management-system-javafx/HotelManagementSystem

# Build and run
mvn clean javafx:run
```

### Run from IntelliJ IDEA

1. Open the `HotelManagementSystem` folder as a Maven project
2. Let IntelliJ resolve and download dependencies
3. Run `MainApp.java`

## 📄 Documentation

- [Product Requirements Document (PRD)](docs/PRD.md) — Full technical specification
- [Concept Demonstration Guide](docs/demonstration.md) — Maps Java concepts to code implementation

## 👨‍💻 Author

**Adhyayan Rana**  
GitHub: [@adhyayan404rana](https://github.com/adhyayan404rana)

## 📝 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
