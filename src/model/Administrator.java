package model;

public class Administrator extends User {

    public Administrator(String id, String name, String phoneNumber,
                         String password) {
        super(id, name, phoneNumber, password);
    }

    @Override
    public void showMenu() {
        System.out.println("1. View problems by priority");
        System.out.println("2. Assign a repair worker");
        System.out.println("3. Logout");
    }
}
