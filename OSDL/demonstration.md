# Hotel Management System — Lab Demonstration (Weeks 1–9)

This document maps the theoretical concepts covered in Labs 1 through 9 to their practical implementation within the Advanced Hotel Management System. It serves as a visual and technical guide for presenting the project.

---

## 📸 1. Main Dashboard & UI Layouts
**Concepts Applied:** JavaFX GUI & Layouts (Weeks 9 & 10)
- **Layout Variety:** The application utilizes distinct layout managers like `BorderPane` (for overall structure), `GridPane` (for forms), and `VBox`/`HBox` (for alignment).
- **Components:** JavaFX controls such as `TableView`, `ComboBox`, `TextField`, and `Button` are integrated to create an intuitive user interface.
- **Event Handling:** Button clicks (e.g., "Manage Rooms", "Billing") trigger actions via Controller classes (`DashboardController`, `RoomManagementController`).

> **[INSERT DASHBOARD SCREENSHOT HERE]**
> *Caption: The main dashboard showcasing live statistics and system navigation.*

---

## 🧵 2. Live Background Tasks (The Clock)
**Concepts Applied:** Multithreading (Weeks 3 & 4)
- **Background Tasks:** A live background thread (`ClockTask` implementing the `Runnable` interface) continuously updates the clock on the dashboard without blocking the main JavaFX UI thread.
- **Daemon Threading:** The clock runs as a daemon thread, ensuring it safely terminates when the main application closes.

> **[INSERT SCREENSHOT HIGHLIGHTING THE LIVE CLOCK HERE]**
> *Caption: The real-time clock actively running on a background thread (`ClockTask.java`).*

---

## 🏛️ 3. Core Architecture & Models
**Concepts Applied:** Object-Oriented Programming (Week 1)
- **Encapsulation:** Model classes (like `Room`, `Customer`) enforce data hiding using `private` fields, exposing state safely through `public` getters and setters.
- **Inheritance & Polymorphism:** An abstract base class `Room` is extended by concrete classes (e.g., `StandardRoom`, `LuxuryRoom`). This allows uniform treatment of different room types.
- **Abstraction:** The implementation utilizes an `Amenities` interface (e.g., `provideWifi()`) to contractually obligate certain behaviors in sub-classes.

> **[INSERT SCREENSHOT OF YOUR IDE SHOWING ROOM / STANDARDROOM CLASSES HERE - Optional]**
> *Caption: The class hierarchy demonstrating Inheritance and Polymorphism.*

---

## 📦 4. Type Safety & Financial Calculations
**Concepts Applied:** Enumerations & Wrapper Classes (Week 2)
- **Enums:** The `RoomType` enum defines standardized room categories (e.g., `STANDARD`, `DELUXE`) and is initialized with base tariff values via an enum constructor.
- **Wrappers:** Wrapper classes like `Integer` (for IDs/Room Numbers) and `Double` (for tariffs and totals) are used instead of primitives to seamlessly integrate with Java Collections and avoid precision issues in null contexts.

---

## 🗂️ 5. In-Memory Data Management & Display
**Concepts Applied:** Generics (Week 7) & Collections Framework (Week 8)
- **Data Structures:** `ArrayList` is used extensively to dynamically populate the JavaFX `TableView` components (e.g., in `DashboardController.roomStore`).
- **Mapping:** A `HashMap` is utilized for quick, key-value lookups (e.g., managing system configurations or active guest sessions).
- **Sorting:** Built-in list sorting mechanisms (like `Collections.sort()`) are implemented to order displayed data, such as sorting rooms by price or availability.
- **Generic Classes:** A custom `Pair<T, U>` generic utility class is utilized to strongly type associations (e.g., temporarily binding a Customer ID to a Room ID).

> **[INSERT SCREENSHOT OF ROOM MANAGEMENT TABLE VIEW HERE]**
> *Caption: Dynamic Data display utilizing ArrayLists and JavaFX TableViews.*

---

## 🔒 6. Concurrency Control
**Concepts Applied:** Synchronization (Weeks 3 & 4)
- **Synchronization:** Critical sections of the code, such as the room booking logic, utilize the `synchronized` keyword. This ensures data integrity by preventing two concurrent threads from booking the exact same room simultaneously.

---

## 💾 7. File System Integration & Persistence
**Concepts Applied:** I/O Streams, RandomAccessFile & Serialization (Weeks 5 & 6)
- **I/O Streams:** Upon customer checkout, a formatted, itemized text receipt is generated and written to the `invoices/` directory using character streams (`FileWriter` in `FileIOUtility.java`).
- **Serialization:** Application configurations are serialized and saved to a binary file (`app_config.dat`) using `ObjectOutputStream`, allowing state preservation across restarts.
- **RandomAccessFile:** The system maintains an append-only binary event log (`system_events.log`), writing actions quickly to the end of the file using `RandomAccessFile`.

> **[INSERT SCREENSHOT OF GENERATED TEXT INVOICE / TERMINAL SHOWING LOG EVENTS HERE]**
> *Caption: The checkout process triggers file generation and binary log appends.*

---

### End of Demonstration
*Database integration (JDBC) via SQL schemas stands ready for subsequent integration phases.*
