package sa.edu.kau.fcit.cpit252.project.store;
import sa.edu.kau.fcit.cpit252.project.model.User;

public interface UserStore {
    void save(User user);

    User findByEmail(String email);
}
