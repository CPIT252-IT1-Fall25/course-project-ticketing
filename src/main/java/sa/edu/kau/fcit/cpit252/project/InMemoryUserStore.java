package sa.edu.kau.fcit.cpit252.project;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserStore implements UserStore {

    private static final InMemoryUserStore INSTANCE = new InMemoryUserStore();

    private final Map<String, User> users = new ConcurrentHashMap<>();

    private InMemoryUserStore() {
    }

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
