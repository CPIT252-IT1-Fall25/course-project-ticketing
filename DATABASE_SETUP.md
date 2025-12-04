# Database Setup Guide

## Azure SQL Database Configuration

This project uses Azure SQL Database to store movie, show, and booking information.

### 1. Create Database Schema

Run the `database-schema.sql` file in your Azure SQL Database to create the necessary tables:

```sql
-- Execute database-schema.sql in Azure SQL Database
```

### 2. Configure Database Connection

Update the database connection settings in `DatabaseConnection.java`:

**Option 1: Environment Variables (Recommended for Azure)**
Set these environment variables in your Azure App Service:
- `DB_URL`: Your Azure SQL connection string
- `DB_USER`: Database username
- `DB_PASSWORD`: Database password

**Option 2: Direct Configuration**
Edit `src/main/java/sa/edu/kau/fcit/cpit252/project/DatabaseConnection.java` and update:
```java
private static final String DB_URL = "jdbc:sqlserver://your-server.database.windows.net:1433;database=your-database;encrypt=true;...";
private static final String DB_USER = "your-username";
private static final String DB_PASSWORD = "your-password";
```

### 3. Azure SQL Connection String Format

```
jdbc:sqlserver://[server].database.windows.net:1433;database=[database];encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
```

### 4. Image Storage

Movie images should be stored on Azure Blob Storage or Azure Disk. Update image URLs in the database to point to your Azure storage location.

Example:
- Azure Blob Storage: `https://[storage-account].blob.core.windows.net/images/movie1.jpg`
- Azure Disk: Store images in a web-accessible directory

### 5. Database Tables

- **Movies**: Stores movie information (name, description, image URL)
- **Shows**: Stores show information (movie + location + time combination) with seat counts
- **Bookings**: Stores booking transactions

Each show (movie + location + time) has its own separate seat inventory.

### 6. Testing

After setup, test the connection by:
1. Creating a movie in the database
2. Creating a show for that movie
3. Making a test booking

The system will automatically create shows when they don't exist, with a default of 100 seats.


