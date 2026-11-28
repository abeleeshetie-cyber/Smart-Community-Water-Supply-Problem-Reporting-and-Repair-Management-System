package service;

import model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    // all users (needed by the database layer to save them)
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // returns the user if ID and password are correct, otherwise null
    public User login(String id, String password) {
        User user = findById(id);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }
}
