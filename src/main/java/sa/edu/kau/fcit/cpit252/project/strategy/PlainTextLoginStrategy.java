package sa.edu.kau.fcit.cpit252.project.strategy;
import sa.edu.kau.fcit.cpit252.project.model.User;
public class PlainTextLoginStrategy  implements  LoginStrategy {
    
    @Override
    public boolean authenticate(User user, String password) {
        return user.getPassword().equals(password);
    }
}



