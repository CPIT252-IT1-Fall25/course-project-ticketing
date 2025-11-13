package sa.edu.kau.fcit.cpit252.project;

public class AuthService {

    private final UserStore userStore;

    // Dependency injected user store (we will pass the singleton)
    public AuthService(UserStore userStore) {
        this.userStore = userStore;
    }

    public boolean register(String name, String email, String password) {
        if (userStore.findByEmail(email) != null) {
            return false; // user already exists
        }
        userStore.save(new User(name, email, password));
        return true;
    }

    public boolean login(String email, String password) {
        User user = userStore.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
