# Airport Simulator GUI

**Airport Simulator GUI** is a **Java 17-based** simulation and visualization program that models the passenger flow through various airport service points (check-in, baggage drop, security, etc.).  
The project utilizes **JavaFX** for the graphical user interface, a **discrete event simulation engine** for core logic, and a **MariaDB/Hibernate-based database** for storing and analyzing results.

---

## Project Overview

The simulator aims to model the movement of passengers through the airport process chain, measure process efficiency, and visualize results in real time.  
The program follows the **Model-View-Controller (MVC)** architecture, where the simulation logic, user interface, and database layers are separated for clarity and maintainability.

### Key Features
- Discrete event–driven simulation engine  
- Real-time passenger and queue visualization with JavaFX  
- Automatic result storage to a MariaDB database (`Run` and `RunStatistics`)  
- Historical simulation browsing via the GUI  
- Statistical metrics: average, maximum, and duration values  
- Randomized service times (via the *eduni distributions* library)

---

## Architecture (MVC)

The project is structured around a clear **Model–View–Controller** design pattern:

- **Model (`simu/`):**  
  Contains the simulation engine (`MyEngine`), event list (`EventList`), simulation clock (`Clock`), and model components (customers, service points).

- **View (`view/`):**  
  JavaFX-based user interface that visualizes queues and passenger movement on a Canvas.  
  Uses the `IVisualisation` interface to decouple rendering logic from simulation logic.

- **Controller (`controller/`):**  
  Mediates between the UI and the simulation model. Handles simulation start/stop and saving results to the database.

- **Database Layer (`dao/`, `entity/`):**  
  Contains the JPA entities `Run` and `RunStatistics` and their data access objects (`RunDao`, `RunStatisticsDao`).

---

## Setup and Usage

### 1. Requirements
- **JDK 17** or newer  
- **Apache Maven**  
- **MariaDB** (or another compatible SQL database)

### 2. Clone and Build
```bash
git clone https://github.com/MustBeViable/Airport_simulator_GUI.git
cd Airport_simulator_GUI
mvn clean install
```

3. Run the simulator
```bash
mvn javafx:run
```

4. Configure Database
```bash
src/main/resources/META-INF/persistence.xml
```
```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:mariadb://localhost:3306/airport_simulator"/>
<property name="jakarta.persistence.jdbc.user" value="appuser"/>
<property name="jakarta.persistence.jdbc.password" value="password"/>
```
And run 
```bash
src/main/resources/SQL_Scripts/database_creation_script.sql
```
