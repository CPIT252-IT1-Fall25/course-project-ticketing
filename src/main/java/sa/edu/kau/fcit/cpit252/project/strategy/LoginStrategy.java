package sa.edu.kau.fcit.cpit252.project.strategy;

import sa.edu.kau.fcit.cpit252.project.model.User;

/**
 * Strategy Pattern - Interface for authentication strategies.
 * Allows pluggable authentication methods (plain text, hashed, OAuth, etc.)
 * 
 * Design Pattern: Strategy
 * - Defines a family of algorithms (authentication methods)
 * - Encapsulates each algorithm
 * - Makes them interchangeable at runtime
 */
public interface LoginStrategy {
    
    /**
     * Authenticates a user with the given password.
     * 
     * @param user The user to authenticate
     * @param password The password to verify
     * @return true if authentication is successful, false otherwise
     */
    boolean authenticate(User user, String password);
}
