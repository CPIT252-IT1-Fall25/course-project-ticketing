package sa.edu.kau.fcit.cpit252.project.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Azure SQL Database connection parameters
    // Update these with your Azure SQL connection details
    private static final String DB_URL = System.getenv("DB_URL") != null 
        ? System.getenv("DB_URL") 
        : "jdbc:sqlserver://your-server.database.windows.net:1433;database=your-database;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    
    private static final String DB_USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") 
        : "your-username";
    
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") 
        : "your-password";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server JDBC Driver not found", e);
        }
    }
}


