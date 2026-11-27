package ui;

import model.Administrator;
import model.RepairWorker;
import model.Resident;
import model.User;
import service.UserManager;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private UserManager userManager;
    private JTextField idField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame(UserManager userManager) {
        this.userManager = userManager;

        setTitle("Water Supply System - Login");
        setSize(360, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);          // center on screen
        setLayout(new BorderLayout());

        // form: ID, password, button
        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        idField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        form.add(new JLabel("User ID:"));
        form.add(idField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        form.add(new JLabel(""));
        form.add(loginButton);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);

        add(form, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String id = idField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = userManager.login(id, password);

        if (user == null) {
            messageLabel.setText("Wrong ID or password");
            return;
        }

        messageLabel.setText("Welcome, " + user.getName());
        openPanelFor(user);
    }

    // Thursday integration: connect your friends' screens here
    private void openPanelFor(User user) {
        if (user instanceof Resident) {
            // Bonsa's ResidentPanel goes here, for example:
            // new ResidentPanel(user).setVisible(true);
            JOptionPane.showMessageDialog(this, "Resident screen coming soon");
        } else if (user instanceof Administrator) {
            // Amin's AdminPanel goes here
            JOptionPane.showMessageDialog(this, "Admin screen coming soon");
        } else if (user instanceof RepairWorker) {
            // Abduba's WorkerPanel goes here
            JOptionPane.showMessageDialog(this, "Worker screen coming soon");
        }
    }
}