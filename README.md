# Neurotherapy Dashboard

A comprehensive Patient Management System designed for Neurotherapy Clinics to manage patient records, sessions, and medical history.

## Architecture

The application follows a clean layered architecture with clear separation of concerns:

```
com.neuro
├── application/         # Application Layer
│   └── NeuroApplication.java    # Main entry point
│
├── db/                  # Database Layer
│   └── DBConnection.java        # Database connection management
│
├── dao/                 # Data Access Layer (DAO)
│   ├── UserDAO.java            # User authentication & management
│   ├── PatientDAO.java         # Patient CRUD operations
│   └── SessionDAO.java         # Session management
│
├── model/               # Model/Domain Layer
│   ├── Patient.java            # Patient entity
│   └── PatientColumns.java     # Column definitions
│
└── ui/                  # User Interface Layer
    ├── LoginFrame.java         # User login
    ├── SignupFrame.java        # User registration
    ├── DoctorDashboard.java    # Main dashboard
    ├── PatientForm.java        # Patient form with PDF export
    ├── PatientDetailsFrame.java # Patient details view
    ├── PatientHistoryFormMySQL.java # Patient history form
    └── SessionFormDialog.java  # Session management dialog
```

## Layer Responsibilities

### 1. Application Layer (`com.neuro.application`)
- **Purpose**: Bootstrap and initialization
- **Entry Point**: `NeuroApplication.java`
- **Responsibilities**: Application startup, UI initialization, system configuration

### 2. Database Layer (`com.neuro.db`)
- **Purpose**: Database connectivity
- **Files**: `DBConnection.java`
- **Responsibilities**: Connection pooling, database configuration, connection management
- **Configuration**: Reads from `db.properties`

### 3. Data Access Layer (`com.neuro.dao`)
- **Purpose**: Database operations
- **Files**: `UserDAO.java`, `PatientDAO.java`, `SessionDAO.java`
- **Responsibilities**: CRUD operations, SQL queries, data persistence
- **Pattern**: Static methods for data access

### 4. Model Layer (`com.neuro.model`)
- **Purpose**: Domain entities
- **Files**: `Patient.java`, `PatientColumns.java`
- **Responsibilities**: Data structures, business entities, getters/setters

### 5. UI Layer (`com.neuro.ui`)
- **Purpose**: User interface
- **Technology**: Java Swing
- **Responsibilities**: User interaction, form handling, UI rendering
- **Components**: Frames, dialogs, panels, event handlers

## Building the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Build Commands

```bash
# Clean previous builds
mvn clean

# Compile and package
mvn package

# This creates Dashboard.jar in the target/ directory
```

### Running the Application

```bash
# Run the executable JAR
java -jar target/Dashboard.jar
```

## Database Configuration

Edit `src/db.properties` with your MySQL credentials:

```properties
db.url=jdbc:mysql://127.0.0.1:3306/clinic?useSSL=false&serverTimezone=UTC
db.username=root
db.password=yourpassword
```

## Dependencies

- **MySQL Connector J**: JDBC driver for MySQL connectivity
- **Apache PDFBox**: PDF generation for patient reports

## Features

- User authentication and authorization
- Patient registration with comprehensive medical history
- Pain point tracking and visualization
- Session management with before/after pain tracking
- Vitals monitoring (BP, Pulse, O2, Temperature)
- PDF export functionality for patient records
- Report upload and analysis
- Search functionality by mobile number

## Development Guidelines

1. **Layer Separation**: Never access database directly from UI. Always use DAO layer.
2. **Database Layer**: Only `DBConnection` should handle connection management.
3. **DAO Layer**: Keep business logic minimal. Focus on data access.
4. **Model Layer**: Pure POJOs with no business logic.
5. **UI Layer**: No SQL queries. Delegate all data operations to DAO layer.
6. **Application Layer**: Minimal logic, just bootstrap.

## Project Structure

Follows standard Maven project layout:

```
Neurotherapy/
├── src/
│   └── main/
│       ├── java/                       # Java source files
│       │   ├── TestMySQL.java         # Database test utility
│       │   └── com/neuro/
│       │       ├── application/       # Application entry point
│       │       │   └── NeuroApplication.java
│       │       ├── db/                # Database layer
│       │       │   └── DBConnection.java
│       │       ├── dao/               # Data Access Objects
│       │       │   ├── UserDAO.java
│       │       │   ├── PatientDAO.java
│       │       │   └── SessionDAO.java
│       │       ├── model/             # Domain models
│       │       │   ├── Patient.java
│       │       │   └── PatientColumns.java
│       │       └── ui/                # User Interface
│       │           ├── LoginFrame.java
│       │           ├── SignupFrame.java
│       │           ├── DoctorDashboard.java
│       │           ├── PatientForm.java
│       │           ├── PatientDetailsFrame.java
│       │           ├── PatientHistoryFormMySQL.java
│       │           └── SessionFormDialog.java
│       └── resources/                  # Resource files
│           ├── db.properties          # Database configuration
│           └── META-INF/
│               └── MANIFEST.MF        # JAR manifest
├── target/                             # Build output
│   └── Dashboard.jar                   # Executable JAR (8.2 MB)
├── pom.xml                             # Maven configuration
├── build.xml                           # Ant build (legacy - deprecated)
└── README.md                           # This file
```

## License

Proprietary - Neurotherapy Clinic Management System
