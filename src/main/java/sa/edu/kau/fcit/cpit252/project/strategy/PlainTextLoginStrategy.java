package sa.edu.kau.fcit.cpit252.project.strategy;

import sa.edu.kau.fcit.cpit252.project.model.User;

/**
 * Strategy Pattern - Concrete strategy for plain text password authentication.
 * 
 * Design Pattern: Strategy (Concrete Implementation)
 * This is a simple implementation that compares passwords directly.
 * In production, use HashedLoginStrategy instead.
 */
public class PlainTextLoginStrategy implements LoginStrategy {
    
    @Override
    public boolean authenticate(User user, String password) {
        if (user == null || password == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }
}

