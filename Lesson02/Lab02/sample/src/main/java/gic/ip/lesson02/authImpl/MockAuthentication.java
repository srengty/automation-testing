package gic.ip.lesson02.authImpl;

import java.util.ArrayList;
import java.util.List;

import gic.ip.lesson02.auth.AuthenticationService;
import gic.ip.lesson02.model.User;

public class MockAuthentication implements AuthenticationService {
    static List<User> users = new ArrayList<>() {
        {
            add(new User("pagna", "123456"));
            add(new User("naieang", "123456", true));
        }
    };

    @Override
    public Object attempt(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) &&
                    user.getPassword().equals(password) && !user.isBanned()) {
                return user;
            }
        }
        return null;
    }

}
