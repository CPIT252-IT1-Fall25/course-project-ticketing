package sa.edu.kau.fcit.cpit252.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sa.edu.kau.fcit.cpit252.project.model.User;
import sa.edu.kau.fcit.cpit252.project.strategy.HashedLoginStrategy;
import sa.edu.kau.fcit.cpit252.project.strategy.LoginStrategy;
import sa.edu.kau.fcit.cpit252.project.strategy.PlainTextLoginStrategy;

/**
 * Tests for the Strategy Pattern implementation.
 * Verifies that different authentication strategies work correctly.
 */
@DisplayName("Strategy Pattern Tests")
public class StrategyPatternTest {
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User("Test User", "test@example.com", "password123");
    }
    
    @Test
    @DisplayName("PlainTextLoginStrategy authenticates with correct password")
    void testPlainTextStrategyCorrectPassword() {
        LoginStrategy strategy = new PlainTextLoginStrategy();
        assertTrue(strategy.authenticate(testUser, "password123"));
    }
    
    @Test
    @DisplayName("PlainTextLoginStrategy rejects wrong password")
    void testPlainTextStrategyWrongPassword() {
        LoginStrategy strategy = new PlainTextLoginStrategy();
        assertFalse(strategy.authenticate(testUser, "wrongpassword"));
    }
    
    @Test
    @DisplayName("PlainTextLoginStrategy handles null user")
    void testPlainTextStrategyNullUser() {
        LoginStrategy strategy = new PlainTextLoginStrategy();
        assertFalse(strategy.authenticate(null, "password123"));
    }
    
    @Test
    @DisplayName("PlainTextLoginStrategy handles null password")
    void testPlainTextStrategyNullPassword() {
        LoginStrategy strategy = new PlainTextLoginStrategy();
        assertFalse(strategy.authenticate(testUser, null));
    }
    
    @Test
    @DisplayName("HashedLoginStrategy hashes passwords correctly")
    void testHashedStrategyHashesPassword() {
        HashedLoginStrategy strategy = new HashedLoginStrategy();
        String hashed = strategy.hashPassword("password123");
        
        assertNotNull(hashed);
        assertNotEquals("password123", hashed);
        // Same password should produce same hash
        assertEquals(hashed, strategy.hashPassword("password123"));
    }
    
    @Test
    @DisplayName("HashedLoginStrategy produces different hashes for different passwords")
    void testHashedStrategyDifferentPasswords() {
        HashedLoginStrategy strategy = new HashedLoginStrategy();
        String hash1 = strategy.hashPassword("password123");
        String hash2 = strategy.hashPassword("password456");
        
        assertNotEquals(hash1, hash2);
    }
    
    @Test
    @DisplayName("HashedLoginStrategy authenticates with hashed password")
    void testHashedStrategyAuthentication() {
        HashedLoginStrategy strategy = new HashedLoginStrategy();
        String hashedPassword = strategy.hashPassword("password123");
        
        User hashedUser = new User("Test", "test@test.com", hashedPassword);
        assertTrue(strategy.authenticate(hashedUser, "password123"));
    }
    
    @Test
    @DisplayName("Strategies are interchangeable at runtime")
    void testStrategyInterchangeability() {
        // This demonstrates the Strategy pattern - same interface, different implementations
        LoginStrategy plainStrategy = new PlainTextLoginStrategy();
        LoginStrategy hashedStrategy = new HashedLoginStrategy();
        
        // Plain text user
        User plainUser = new User("Plain", "plain@test.com", "secret");
        assertTrue(plainStrategy.authenticate(plainUser, "secret"));
        
        // Hashed user
        String hashedPassword = ((HashedLoginStrategy) hashedStrategy).hashPassword("secret");
        User hashedUser = new User("Hashed", "hashed@test.com", hashedPassword);
        assertTrue(hashedStrategy.authenticate(hashedUser, "secret"));
        
        // Both implement the same interface
        assertTrue(plainStrategy instanceof LoginStrategy);
        assertTrue(hashedStrategy instanceof LoginStrategy);
    }
}


