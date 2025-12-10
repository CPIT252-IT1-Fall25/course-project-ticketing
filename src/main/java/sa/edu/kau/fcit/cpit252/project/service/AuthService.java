package sa.edu.kau.fcit.cpit252.project.service;
import sa.edu.kau.fcit.cpit252.project.model.User;
import sa.edu.kau.fcit.cpit252.project.store.UserStore;
public class AuthService {

    private final UserStore userStore;

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
    
    public User loginAndGetUser(String email, String password) {
        User user = userStore.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
