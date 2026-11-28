package ui;

import model.Administrator;
import model.ProblemReport;
import model.RepairWorker;
import model.Status;
import service.PriorityCalculator;
import service.ReportService;
import service.WorkerAssigner;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JFrame {

    private final Administrator admin;
    private final ReportService reportService;
    private final PriorityCalculator calculator;
    private final WorkerAssigner assigner;

    private final DefaultTableModel tableModel;
    private final JTable table;

    public AdminPanel(Administrator admin, ReportService reportService,
                      PriorityCalculator calculator, WorkerAssigner assigner) {
        this.admin = admin;
        this.reportService = reportService;
        this.calculator = calculator;
        this.assigner = assigner;

        setTitle("Admin: " + admin.getName());
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"ID", "Location", "Type", "Severity", "Households", "Status", "Score"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTable());

        JButton assignButton = new JButton("Assign worker");
        assignButton.addActionListener(e -> assignSelectedReport());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(refreshButton);
        buttons.add(assignButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        refreshTable();
        setVisible(true);
    }

    // fills the table, highest score first
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<ProblemReport> sorted = calculator.getPrioritized(reportService.getAllReports());
        for (ProblemReport r : sorted) {
            tableModel.addRow(new Object[]{
                    r.getReportId(),
                    r.getLocation(),
                    r.getType(),
                    r.getSeverity(),
                    r.getAffectedHouseholds(),
                    r.getStatus(),
                    String.format("%.1f", r.getPriorityScore())
            });
        }
    }

    private void assignSelectedReport() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a report first.");
            return;
        }

        int reportId = (int) tableModel.getValueAt(row, 0);
        ProblemReport report = reportService.findById(reportId);
        if (report == null) {
            JOptionPane.showMessageDialog(this, "Report not found.");
            return;
        }
        if (report.getStatus() != Status.SUBMITTED) {
            JOptionPane.showMessageDialog(this, "This report is already " + report.getStatus() + ".");
            return;
        }

        List<RepairWorker> workers = assigner.suggestWorkers(report);
        if (workers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No worker available for this problem right now.");
            return;
        }

        String[] names = new String[workers.size()];
        for (int i = 0; i < workers.size(); i++) {
            names[i] = workers.get(i).getName() + " (" + workers.get(i).getTrade() + ")";
        }

        Object choice = JOptionPane.showInputDialog(this, "Choose a worker for report " + reportId + ":",
                "Assign worker", JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
        if (choice == null) {
            return; // admin cancelled
        }

        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(choice)) {
                boolean done = assigner.assign(report, workers.get(i));
                if (!done) {
                    JOptionPane.showMessageDialog(this, "Could not assign this worker.");
                }
                break;
            }
        }
        refreshTable();
    }
}
