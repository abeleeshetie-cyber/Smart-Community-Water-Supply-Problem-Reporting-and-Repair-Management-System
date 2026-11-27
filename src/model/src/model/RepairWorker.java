package model;

public class RepairWorker extends User {
    private String trade;
    private boolean available;

    public RepairWorker(String id, String name, String phoneNumber,
                        String password, String trade) {
        super(id, name, phoneNumber, password);
        this.trade = trade;
        this.available = true;
    }

    public String getTrade() { return trade; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public void showMenu() {
        System.out.println("1. View my assigned jobs");
        System.out.println("2. Start a repair");
        System.out.println("3. Mark a job as resolved");
        System.out.println("4. Logout");
    }
}
