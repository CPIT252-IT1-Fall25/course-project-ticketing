package sa.edu.kau.fcit.cpit252.project.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sa.edu.kau.fcit.cpit252.project.model.Booking;
import sa.edu.kau.fcit.cpit252.project.store.DatabaseConnection;

/**
 * Service for managing bookings in the database.
 */
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
        
        // Create booking using Singleton DatabaseConnection
        String sql = "INSERT INTO Bookings (showId, userEmail, ticketType, ticketQuantity, popcornQuantity, totalPrice) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.createConnection();
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
    
    /**
     * Get all bookings for a user by email.
     */
    public List<Booking> getBookingsByUser(String userEmail) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Bookings WHERE userEmail = ? ORDER BY bookingDate DESC";
        
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userEmail);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("bookingId"));
                    booking.setShowId(rs.getInt("showId"));
                    booking.setUserEmail(rs.getString("userEmail"));
                    booking.setTicketType(rs.getString("ticketType"));
                    booking.setTicketQuantity(rs.getInt("ticketQuantity"));
                    booking.setPopcornQuantity(rs.getInt("popcornQuantity"));
                    booking.setTotalPrice(rs.getBigDecimal("totalPrice"));
                    bookings.add(booking);
                }
            }
        }
        
        return bookings;
    }
    
    /**
     * Get a booking by ID.
     */
    public Booking getBookingById(int bookingId) throws SQLException {
        String sql = "SELECT * FROM Bookings WHERE bookingId = ?";
        
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("bookingId"));
                    booking.setShowId(rs.getInt("showId"));
                    booking.setUserEmail(rs.getString("userEmail"));
                    booking.setTicketType(rs.getString("ticketType"));
                    booking.setTicketQuantity(rs.getInt("ticketQuantity"));
                    booking.setPopcornQuantity(rs.getInt("popcornQuantity"));
                    booking.setTotalPrice(rs.getBigDecimal("totalPrice"));
                    return booking;
                }
            }
        }
        
        return null;
    }
}


