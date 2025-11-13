package sa.edu.kau.fcit.cpit252.project;

public interface UserStore {
    void save(User user);

    User findByEmail(String email);
}
