package sa.edu.kau.fcit.cpit252.project.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Thread-safe Singleton pattern implementation for Database Connection.
 * Uses double-checked locking for lazy initialization.
 */
public class DatabaseConnection {
    
    // Volatile ensures visibility across threads
    private static volatile DatabaseConnection instance;
    
    // Connection pool would be better for production, but this demonstrates Singleton
    private Connection connection;
    
    // Azure SQL Database connection parameters
    private static final String DB_URL = System.getenv("DB_URL") != null 
        ? System.getenv("DB_URL") 
        : "jdbc:sqlserver://fakenetflix-server.database.windows.net:1433;database=free-sql-db-4024107;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    
    private static final String DB_USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") 
        : "adminuser";
    
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") 
        : "YourStrongP@ssw0rd!";

    // Private constructor prevents instantiation from outside
    private DatabaseConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQL Server JDBC Driver not found", e);
        }
    }
    
    /**
     * Returns the singleton instance using double-checked locking.
     * This is thread-safe and lazy-initialized.
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Gets a database connection. Creates a new one if the current one is closed or null.
     * For production use, consider using a connection pool like HikariCP.
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        return connection;
    }
    
    /**
     * Static convenience method to get a new connection.
     * Each call creates a fresh connection (recommended for servlets to avoid threading issues).
     */
    public static Connection createConnection() throws SQLException {
        getInstance(); // Ensure driver is loaded
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Closes the singleton connection if open.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Log error in production
            }
            connection = null;
        }
    }
    
    // Prevent cloning of singleton
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone singleton DatabaseConnection");
    }
}


