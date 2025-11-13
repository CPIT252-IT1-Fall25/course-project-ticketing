package sa.edu.kau.fcit.cpit252.project;

public class Signup {

    private final AuthService authService;

    // Default constructor: uses the singleton store
    public Signup() {
        this.authService = new AuthService(InMemoryUserStore.getInstance());
    }

    // Extra constructor to allow tests to inject mocks if needed
    public Signup(AuthService authService) {
        this.authService = authService;
    }

    public boolean handleSignup(String name, String email, String password) {
        return authService.register(name, email, password);
    }
}
