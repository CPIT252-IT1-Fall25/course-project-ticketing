package sa.edu.kau.fcit.cpit252.project.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sa.edu.kau.fcit.cpit252.project.model.User;

/**
 * In-memory implementation of UserStore using Singleton Pattern.
 * 
 * Design Pattern: Singleton
 * - Private constructor prevents external instantiation
 * - Static getInstance() provides global access point
 * - Thread-safe with ConcurrentHashMap
 */
public class InMemoryUserStore implements UserStore {

    private static final InMemoryUserStore INSTANCE = new InMemoryUserStore();

    private final Map<String, User> users = new ConcurrentHashMap<>();

    private InMemoryUserStore() {
        // Private constructor for Singleton
    }

    /**
     * Returns the singleton instance.
     * 
     * @return The single InMemoryUserStore instance
     */
    public static InMemoryUserStore getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(User user) {
        if (user != null && user.getEmail() != null) {
            users.put(user.getEmail(), user);
        }
    }

    @Override
    public User findByEmail(String email) {
        if (email == null) {
            return null;
        }
        return users.get(email);
    }
    
    /**
     * Clears all stored users. Useful for testing.
     */
    public void clear() {
        users.clear();
    }
    
    /**
     * Returns the number of stored users.
     * 
     * @return User count
     */
    public int size() {
        return users.size();
    }
}
