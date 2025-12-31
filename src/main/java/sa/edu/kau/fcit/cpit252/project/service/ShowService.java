package sa.edu.kau.fcit.cpit252.project.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sa.edu.kau.fcit.cpit252.project.model.Show;
import sa.edu.kau.fcit.cpit252.project.store.DatabaseConnection;

/**
 * Service for managing movie shows in the database.
 */
public class ShowService {
    
    /**
     * Get or create a show for a specific movie, location, and time
     * Returns the showId
     */
    public int getOrCreateShow(int movieId, String location, String showTime, String hallType) throws SQLException {
        String sql = "SELECT showId, availableSeats FROM Shows WHERE movieId = ? AND location = ? AND showTime = ?";
        
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, movieId);
            stmt.setString(2, location);
            stmt.setString(3, showTime);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("showId");
                }
            }
            
            // Show doesn't exist, create it
            String insertSql = "INSERT INTO Shows (movieId, location, showTime, hallType, totalSeats, availableSeats) " +
                             "VALUES (?, ?, ?, ?, 100, 100)";
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, movieId);
                insertStmt.setString(2, location);
                insertStmt.setString(3, showTime);
                insertStmt.setString(4, hallType);
                insertStmt.executeUpdate();
                
                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        
        throw new SQLException("Failed to create show");
    }
    
    /**
     * Get available seats for a specific show
     */
    public int getAvailableSeats(int showId) throws SQLException {
        String sql = "SELECT availableSeats FROM Shows WHERE showId = ?";
        
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, showId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("availableSeats");
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Check if enough seats are available and reserve them atomically
     * Returns true if seats were successfully reserved, false otherwise
     */
    public boolean reserveSeats(int showId, int quantity) throws SQLException {
        String sql = "UPDATE Shows SET availableSeats = availableSeats - ? " +
                    "WHERE showId = ? AND availableSeats >= ?";
        
        try (Connection conn = DatabaseConnection.createConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, quantity);
                stmt.setInt(2, showId);
                stmt.setInt(3, quantity);
                
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    /**
     * Get show details by showId
     */
    public Show getShowById(int showId) throws SQLException {
        String sql = "SELECT * FROM Shows WHERE showId = ?";
        
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, showId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Show show = new Show();
                    show.setShowId(rs.getInt("showId"));
                    show.setMovieId(rs.getInt("movieId"));
                    show.setLocation(rs.getString("location"));
                    show.setShowTime(rs.getString("showTime"));
                    show.setHallType(rs.getString("hallType"));
                    show.setTotalSeats(rs.getInt("totalSeats"));
                    show.setAvailableSeats(rs.getInt("availableSeats"));
                    return show;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get all shows for a movie
     */
    public List<Show> getShowsByMovie(int movieId) throws SQLException {
        List<Show> shows = new ArrayList<>();
        String sql = "SELECT * FROM Shows WHERE movieId = ?";
        
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, movieId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Show show = new Show();
                    show.setShowId(rs.getInt("showId"));
                    show.setMovieId(rs.getInt("movieId"));
                    show.setLocation(rs.getString("location"));
                    show.setShowTime(rs.getString("showTime"));
                    show.setHallType(rs.getString("hallType"));
                    show.setTotalSeats(rs.getInt("totalSeats"));
                    show.setAvailableSeats(rs.getInt("availableSeats"));
                    shows.add(show);
                }
            }
        }
        
        return shows;
    }
}


