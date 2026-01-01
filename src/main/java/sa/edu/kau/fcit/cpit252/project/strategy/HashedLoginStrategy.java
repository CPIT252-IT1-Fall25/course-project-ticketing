package sa.edu.kau.fcit.cpit252.project.strategy;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import sa.edu.kau.fcit.cpit252.project.model.User;

/**
 * Strategy Pattern - Concrete strategy for hashed password authentication.
 * 
 * Design Pattern: Strategy (Concrete Implementation)
 * Uses SHA-256 hashing for secure password comparison.
 * This is more secure than PlainTextLoginStrategy.
 */
public class HashedLoginStrategy implements LoginStrategy {
    
    @Override
    public boolean authenticate(User user, String password) {
        if (user == null || password == null) {
            return false;
        }
        
        String hashedPassword = hashPassword(password);
        return user.getPassword().equals(hashedPassword);
    }
    
    /**
     * Hashes a password using SHA-256.
     * 
     * @param password The plain text password
     * @return Base64 encoded hash of the password
     */
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}


