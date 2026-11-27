package service;

import model.User;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    // key = user id, value = the user
    private Map<String, User> users = new HashMap<>();

    // returns false if the ID already exists
    public boolean addUser(User user) {
        if (users.containsKey(user.getId())) {
            return false;
        }
        users.put(user.getId(), user);
        return true;
    }

    public User findById(String id) {
        return users.get(id);
    }

    // returns the user if ID and password are correct, otherwise null
    public User login(String id, String password) {
        User user = findById(id);              // step 1: find
        if (user != null && user.checkPassword(password)) {   // step 2: check
            return user;
        }
        return null;
    }
}