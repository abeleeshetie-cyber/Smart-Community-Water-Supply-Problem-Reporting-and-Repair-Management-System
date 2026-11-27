import model.Administrator;
import model.RepairWorker;
import model.Resident;
import service.UserManager;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        UserManager manager = new UserManager();

        manager.addUser(new Resident("R001", "Test Resident", "0911000001", "1234", "Bole"));
        manager.addUser(new Administrator("A001", "Test Admin", "0911000002", "admin"));
        manager.addUser(new RepairWorker("W001", "Test Worker", "0911000003", "work", "plumber"));

        javax.swing.SwingUtilities.invokeLater(() ->
                new LoginFrame(manager).setVisible(true));
    }
}