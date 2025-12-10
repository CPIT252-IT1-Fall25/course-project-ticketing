package sa.edu.kau.fcit.cpit252.project.strategy;
import sa.edu.kau.fcit.cpit252.project.model.User;

public interface LoginStrategy {
    boolean authenticate(User user, String password);

}
