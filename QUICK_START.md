# Quick Start Guide - Neurotherapy Dashboard

## Prerequisites

Ensure you have the following installed:
- **Java 17 or higher** - `java -version`
- **Maven 3.6+** - `mvn -version`
- **MySQL 8.0+** - Running on localhost:3306

## Step 1: Configure Database

1. Create the MySQL database:
```sql
CREATE DATABASE clinic;
```

2. Update database credentials in `src/main/resources/db.properties`:
```properties
db.url=jdbc:mysql://127.0.0.1:3306/clinic?useSSL=false&serverTimezone=UTC
db.username=root
db.password=your_actual_password
```

## Step 2: Build the Application

```bash
# Navigate to project directory
cd /path/to/Neurotherapy

# Clean and build
mvn clean package
```

This creates `target/Dashboard.jar` (8.2 MB)

## Step 3: Run the Application

```bash
java -jar target/Dashboard.jar
```

The application will start and show either:
- **Signup screen** (first time - no users exist)
- **Login screen** (if users already exist)

## Common Maven Commands

```bash
# Clean build artifacts
mvn clean

# Compile only (no JAR)
mvn compile

# Build without tests
mvn package -DskipTests

# Full build with tests
mvn clean package

# Install to local Maven repo
mvn install
```

## Troubleshooting

### Issue: "Cannot find main class"
**Solution**: Rebuild with `mvn clean package`

### Issue: "Connection refused to MySQL"
**Solution**: 
1. Ensure MySQL is running: `mysql -u root -p`
2. Check port 3306 is open
3. Verify credentials in `db.properties`

### Issue: "SQL syntax error"
**Solution**: Ensure database schema is created (users table, PatientHistory table, etc.)

### Issue: "Class version error"
**Solution**: You need Java 17+. Check with `java -version`

## Project Structure (Quick Reference)

```
src/main/
├── java/com/neuro/
│   ├── application/  # NeuroApplication.java (main)
│   ├── db/          # DBConnection.java
│   ├── dao/         # UserDAO, PatientDAO, SessionDAO
│   ├── model/       # Patient, PatientColumns
│   └── ui/          # All Swing UI classes
└── resources/
    ├── db.properties        # Database config
    └── META-INF/MANIFEST.MF
```

## Features

- ✅ User authentication and registration
- ✅ Patient management (CRUD operations)
- ✅ Medical history tracking
- ✅ Pain point visualization
- ✅ Vitals monitoring (BP, Pulse, O2, Temperature)
- ✅ Session management (before/after tracking)
- ✅ PDF export for patient records
- ✅ Report upload and analysis
- ✅ Search by mobile number

## Database Tables Required

Ensure your MySQL database has these tables:
1. `users` - User authentication
2. `PatientHistory` - Patient records
3. `NeurotherapySessions` - Therapy sessions

## Support

For issues or questions:
- Check `README.md` for detailed documentation
- Review `REFACTORING_SUMMARY.md` for architecture details
- Examine source code in `src/main/java/com/neuro/`

---

**Happy Coding!** 🚀
