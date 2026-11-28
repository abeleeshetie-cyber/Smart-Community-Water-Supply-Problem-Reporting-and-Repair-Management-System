package ui;

import model.ProblemReport;
import model.ProblemType;
import model.Resident;
import service.ReportService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ResidentPanel extends JFrame {
    private final Resident resident;
    private final ReportService reportService;

    private final JTextField locationField = new JTextField(20);
    private final JComboBox<ProblemType> typeBox = new JComboBox<>(ProblemType.values());
    private final JSpinner severitySpinner = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
    private final JSpinner householdsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Location", "Type", "Severity", "Households", "Status", "Reported"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public ResidentPanel(Resident resident, ReportService reportService) {
        this.resident = resident;
        this.reportService = reportService;

        setTitle("Resident - " + resident.getName());
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Submit form
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Report a water problem"));
        form.add(new JLabel("Location:"));
        form.add(locationField);
        form.add(new JLabel("Problem type:"));
        form.add(typeBox);
        form.add(new JLabel("Severity (1-5):"));
        form.add(severitySpinner);
        form.add(new JLabel("Affected households:"));
        form.add(householdsSpinner);
        form.add(new JLabel(""));
        JButton submitButton = new JButton("Submit Report");
        submitButton.addActionListener(e -> handleSubmit());
        form.add(submitButton);
        add(form, BorderLayout.NORTH);

        // My reports table
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("My reports"));
        add(scrollPane, BorderLayout.CENTER);

        refreshTable();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleSubmit() {
        try {
            reportService.submitReport(
                    resident.getId(),
                    locationField.getText(),
                    (ProblemType) typeBox.getSelectedItem(),
                    (Integer) severitySpinner.getValue(),
                    (Integer) householdsSpinner.getValue());
            locationField.setText("");
            refreshTable();
            JOptionPane.showMessageDialog(this, "Report submitted successfully.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Invalid input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (ProblemReport r : reportService.getReportsByResident(resident.getId())) {
            tableModel.addRow(new Object[]{
                    r.getReportId(),
                    r.getLocation(),
                    r.getType(),
                    r.getSeverity(),
                    r.getAffectedHouseholds(),
                    r.getStatus(),
                    r.getReportedAt().format(fmt)
            });
        }
    }
}
