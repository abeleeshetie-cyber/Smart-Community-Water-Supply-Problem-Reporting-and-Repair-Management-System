package model;

public abstract class User implements Notifiable {
    private String id;
    private String name;
    private String phoneNumber;
    private String password;

    public User(String id, String name, String phoneNumber, String password) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPassword() { return password; }   // needed by the database layer

    public boolean checkPassword(String input) {
        return password.equals(input);
    }

    @Override
    public void notifyUser(String message) {
        System.out.println("Notification for " + name + ": " + message);
    }

    // Each role (Resident, Administrator, RepairWorker) writes its own menu
    public abstract void showMenu();
}
