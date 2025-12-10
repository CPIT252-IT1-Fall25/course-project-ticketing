package sa.edu.kau.fcit.cpit252.project.controller;
import sa.edu.kau.fcit.cpit252.project.model.User;
import sa.edu.kau.fcit.cpit252.project.service.AuthService;
import sa.edu.kau.fcit.cpit252.project.store.InMemoryUserStore;

public class Login {

    private final AuthService authService;

    public Login() {
        this.authService = new AuthService(InMemoryUserStore.getInstance());
    }

    public Login(AuthService authService) {
        this.authService = authService;
    }

    public boolean handleLogin(String email, String password) {
        return authService.login(email, password);
    }
    
    public User handleLoginAndGetUser(String email, String password) {
        return authService.loginAndGetUser(email, password);
    }
}
