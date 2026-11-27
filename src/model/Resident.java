package model;

public class Resident extends User {
    private String address;

    public Resident(String id, String name, String phoneNumber,
                    String password, String address) {
        super(id, name, phoneNumber, password);
        this.address = address;
    }

    public String getAddress() { return address; }

    @Override
    public void showMenu() {
        System.out.println("1. Report a water problem");
        System.out.println("2. Track my reports");
        System.out.println("3. Logout");
    }
}
