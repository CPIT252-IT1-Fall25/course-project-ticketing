package sa.edu.kau.fcit.cpit252.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sa.edu.kau.fcit.cpit252.project.model.User;
import sa.edu.kau.fcit.cpit252.project.service.AuthService;
import sa.edu.kau.fcit.cpit252.project.store.InMemoryUserStore;
import sa.edu.kau.fcit.cpit252.project.store.UserStore;
import sa.edu.kau.fcit.cpit252.project.strategy.HashedLoginStrategy;
import sa.edu.kau.fcit.cpit252.project.strategy.LoginStrategy;
import sa.edu.kau.fcit.cpit252.project.strategy.PlainTextLoginStrategy;

/**
 * Tests for AuthService demonstrating Strategy Pattern usage.
 */
@DisplayName("AuthService Tests (Strategy Pattern)")
public class AuthServiceTest {
    
    private UserStore userStore;
    private AuthService authService;
    
    @BeforeEach
    void setUp() {
        userStore = InMemoryUserStore.getInstance();
        // Clear store before each test to ensure test isolation
        ((InMemoryUserStore) userStore).clear();
        authService = new AuthService(userStore);
    }
    
    @Test
    @DisplayName("Registration creates new user")
    void testRegistration() {
        boolean result = authService.register("John Doe", "john@example.com", "password123");
        
        assertTrue(result);
        assertNotNull(userStore.findByEmail("john@example.com"));
    }
    
    @Test
    @DisplayName("Registration fails for duplicate email")
    void testDuplicateRegistration() {
        authService.register("John", "john@example.com", "pass1");
        boolean result = authService.register("Jane", "john@example.com", "pass2");
        
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Registration fails for null email")
    void testNullEmailRegistration() {
        boolean result = authService.register("John", null, "password");
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Registration fails for empty email")
    void testEmptyEmailRegistration() {
        boolean result = authService.register("John", "  ", "password");
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Login succeeds with correct password (PlainText Strategy)")
    void testLoginWithPlainTextStrategy() {
        authService.register("John", "john@example.com", "secret123");
        
        boolean result = authService.login("john@example.com", "secret123");
        
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Login fails with wrong password")
    void testLoginWrongPassword() {
        authService.register("John", "john@example.com", "secret123");
        
        boolean result = authService.login("john@example.com", "wrongpassword");
        
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Login fails for non-existent user")
    void testLoginNonExistentUser() {
        boolean result = authService.login("nobody@example.com", "password");
        
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Strategy Pattern: Can switch strategy at runtime")
    void testStrategyCanBeSwitched() {
        // Start with PlainText strategy
        assertTrue(authService.getLoginStrategy() instanceof PlainTextLoginStrategy);
        
        // Switch to Hashed strategy
        authService.setLoginStrategy(new HashedLoginStrategy());
        assertTrue(authService.getLoginStrategy() instanceof HashedLoginStrategy);
    }
    
    @Test
    @DisplayName("Strategy Pattern: Hashed strategy authentication")
    void testHashedStrategyAuthentication() {
        HashedLoginStrategy hashedStrategy = new HashedLoginStrategy();
        AuthService hashedAuthService = new AuthService(userStore, hashedStrategy);
        
        // Register with hashed password
        String hashedPassword = hashedStrategy.hashPassword("mypassword");
        userStore.save(new User("Jane", "jane@example.com", hashedPassword));
        
        // Login should work with plain password (strategy handles hashing)
        boolean result = hashedAuthService.login("jane@example.com", "mypassword");
        
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Strategy Pattern: Constructor injection of strategy")
    void testStrategyConstructorInjection() {
        LoginStrategy customStrategy = new PlainTextLoginStrategy();
        AuthService customAuthService = new AuthService(userStore, customStrategy);
        
        assertSame(customStrategy, customAuthService.getLoginStrategy());
    }
    
    @Test
    @DisplayName("loginAndGetUser returns user on success")
    void testLoginAndGetUser() {
        authService.register("John", "john@example.com", "password");
        
        User user = authService.loginAndGetUser("john@example.com", "password");
        
        assertNotNull(user);
        assertEquals("john@example.com", user.getEmail());
        assertEquals("John", user.getName());
    }
    
    @Test
    @DisplayName("loginAndGetUser returns null on failure")
    void testLoginAndGetUserFailure() {
        authService.register("John", "john@example.com", "password");
        
        User user = authService.loginAndGetUser("john@example.com", "wrongpassword");
        
        assertNull(user);
    }
}

