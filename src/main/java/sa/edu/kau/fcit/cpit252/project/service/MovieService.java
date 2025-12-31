package sa.edu.kau.fcit.cpit252.project.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sa.edu.kau.fcit.cpit252.project.model.Movie;
import sa.edu.kau.fcit.cpit252.project.store.DatabaseConnection;

/**
 * Service for managing movies in the database.
 */
public class MovieService {
    
    /**
     * Get movie by ID
     */
    public Movie getMovieById(int movieId) throws SQLException {
        String sql = "SELECT * FROM Movies WHERE movieId = ?";
        
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, movieId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Movie movie = new Movie();
                    movie.setMovieId(rs.getInt("movieId"));
                    movie.setMovieName(rs.getString("movieName"));
                    movie.setDescription(rs.getString("description"));
                    movie.setImageUrl(rs.getString("imageUrl"));
                    return movie;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get movie by name (case-insensitive)
     */
    public Movie getMovieByName(String movieName) throws SQLException {
        String sql = "SELECT * FROM Movies WHERE LOWER(movieName) = LOWER(?)";
        
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, movieName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Movie movie = new Movie();
                    movie.setMovieId(rs.getInt("movieId"));
                    movie.setMovieName(rs.getString("movieName"));
                    movie.setDescription(rs.getString("description"));
                    movie.setImageUrl(rs.getString("imageUrl"));
                    return movie;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Create or get movie by name
     */
    public int getOrCreateMovie(String movieName, String description, String imageUrl) throws SQLException {
        Movie existing = getMovieByName(movieName);
        if (existing != null) {
            return existing.getMovieId();
        }
        
        // Create new movie
        String sql = "INSERT INTO Movies (movieName, description, imageUrl) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, movieName);
            stmt.setString(2, description);
            stmt.setString(3, imageUrl);
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        
        throw new SQLException("Failed to create movie");
    }
    
    /**
     * Get all movies
     */
    public List<Movie> getAllMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM Movies ORDER BY movieName";
        
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movieId"));
                movie.setMovieName(rs.getString("movieName"));
                movie.setDescription(rs.getString("description"));
                movie.setImageUrl(rs.getString("imageUrl"));
                movies.add(movie);
            }
        }
        
        return movies;
    }
}


