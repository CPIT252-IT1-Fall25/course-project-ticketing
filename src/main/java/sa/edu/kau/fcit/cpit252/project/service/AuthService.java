package sa.edu.kau.fcit.cpit252.project.service;

import sa.edu.kau.fcit.cpit252.project.model.User;
import sa.edu.kau.fcit.cpit252.project.store.UserStore;
import sa.edu.kau.fcit.cpit252.project.strategy.LoginStrategy;
import sa.edu.kau.fcit.cpit252.project.strategy.PlainTextLoginStrategy;

/**
 * Authentication service that uses the Strategy Pattern for pluggable authentication.
 * 
 * Design Pattern: Strategy (Context class)
 * - Uses LoginStrategy interface to delegate authentication
 * - Can switch between different authentication methods at runtime
 */
public class AuthService {

    private final UserStore userStore;
    private LoginStrategy loginStrategy;

    /**
     * Creates AuthService with default PlainTextLoginStrategy.
     */
    public AuthService(UserStore userStore) {
        this.userStore = userStore;
        this.loginStrategy = new PlainTextLoginStrategy();
    }
    
    /**
     * Creates AuthService with a custom login strategy.
     * This allows plugging in different authentication methods (Strategy Pattern).
     * 
     * @param userStore The user storage
     * @param loginStrategy The authentication strategy to use
     */
    public AuthService(UserStore userStore, LoginStrategy loginStrategy) {
        this.userStore = userStore;
        this.loginStrategy = loginStrategy;
    }
    
    /**
     * Changes the authentication strategy at runtime.
     * This is a key feature of the Strategy Pattern.
     * 
     * @param loginStrategy The new authentication strategy
     */
    public void setLoginStrategy(LoginStrategy loginStrategy) {
        this.loginStrategy = loginStrategy;
    }

    /**
     * Registers a new user.
     * 
     * @param name User's name
     * @param email User's email
     * @param password User's password
     * @return true if registration successful, false if user already exists
     */
    public boolean register(String name, String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        if (userStore.findByEmail(email) != null) {
            return false; // User already exists
        }
        userStore.save(new User(name, email, password));
        return true;
    }

    /**
     * Authenticates a user using the configured LoginStrategy.
     * Uses Strategy Pattern to delegate authentication logic.
     * 
     * @param email User's email
     * @param password Password to verify
     * @return true if authentication successful
     */
    public boolean login(String email, String password) {
        User user = userStore.findByEmail(email);
        if (user == null) {
            return false;
        }
        // Delegate to Strategy Pattern
        return loginStrategy.authenticate(user, password);
    }
    
    /**
     * Authenticates and returns the user if successful.
     * 
     * @param email User's email
     * @param password Password to verify
     * @return The authenticated User, or null if authentication fails
     */
    public User loginAndGetUser(String email, String password) {
        User user = userStore.findByEmail(email);
        if (user != null && loginStrategy.authenticate(user, password)) {
            return user;
        }
        return null;
    }
    
    /**
     * Gets the currently configured login strategy.
     * 
     * @return The current LoginStrategy
     */
    public LoginStrategy getLoginStrategy() {
        return loginStrategy;
    }
}
