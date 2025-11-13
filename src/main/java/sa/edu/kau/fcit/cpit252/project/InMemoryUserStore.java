package sa.edu.kau.fcit.cpit252.project;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserStore implements UserStore {

    // --- Singleton instance ---
    private static final InMemoryUserStore INSTANCE = new InMemoryUserStore();

    // Internal storage
    private final Map<String, User> users = new ConcurrentHashMap<>();

    // Private constructor: no one can create another instance
    private InMemoryUserStore() {}

    // Global access point
    public static InMemoryUserStore getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(User user) {
        users.put(user.getEmail(), user);
    }

    @Override
    public User findByEmail(String email) {
        return users.get(email);
    }
}
