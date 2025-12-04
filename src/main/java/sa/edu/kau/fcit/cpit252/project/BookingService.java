package sa.edu.kau.fcit.cpit252.project;

import java.math.BigDecimal;
import java.sql.*;

public class BookingService {
    
    /**
     * Create a booking and reduce seats atomically
     * Returns the booking ID if successful, -1 if not enough seats
     */
    public int createBooking(int showId, String userEmail, String ticketType, 
                           int ticketQuantity, int popcornQuantity, BigDecimal totalPrice) throws SQLException {
        
        ShowService showService = new ShowService();
        
        // Check and reserve seats atomically
        if (!showService.reserveSeats(showId, ticketQuantity)) {
            return -1; // Not enough seats
        }
        
        // Create booking
        String sql = "INSERT INTO Bookings (showId, userEmail, ticketType, ticketQuantity, popcornQuantity, totalPrice) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, showId);
            stmt.setString(2, userEmail);
            stmt.setString(3, ticketType);
            stmt.setInt(4, ticketQuantity);
            stmt.setInt(5, popcornQuantity);
            stmt.setBigDecimal(6, totalPrice);
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        
        return -1;
    }
}


