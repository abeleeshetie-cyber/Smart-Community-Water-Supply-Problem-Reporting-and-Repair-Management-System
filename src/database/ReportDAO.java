package database;

import model.MaintenanceRecord;
import model.ProblemReport;
import model.ProblemType;
import model.RepairAssignment;
import model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public void saveReport(ProblemReport report) throws SQLException {
        String sql = "INSERT INTO reports(id,resident_id,location,type,severity,households,"
                + "reported_at,status,priority) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, report.getReportId());
                    statement.setString(2, report.getResidentId());
                    statement.setString(3, report.getLocation());
                    statement.setString(4, report.getType().name());
                    statement.setInt(5, report.getSeverity());
                    statement.setInt(6, report.getAffectedHouseholds());
                    statement.setString(7, report.getReportedAt().toString());
                    statement.setString(8, report.getStatus().name());
                    statement.setDouble(9, report.getPriorityScore());
                    statement.executeUpdate();
                }
                saveAssignment(connection, report);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public void updateReport(ProblemReport report) throws SQLException {
        String sql = "UPDATE reports SET location = ?, type = ?, severity = ?, households = ?, "
                + "status = ?, priority = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, report.getLocation());
                    statement.setString(2, report.getType().name());
                    statement.setInt(3, report.getSeverity());
                    statement.setInt(4, report.getAffectedHouseholds());
                    statement.setString(5, report.getStatus().name());
                    statement.setDouble(6, report.getPriorityScore());
                    statement.setInt(7, report.getReportId());
                    statement.executeUpdate();
                }
                deleteAssignment(connection, report.getReportId());
                saveAssignment(connection, report);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public List<ProblemReport> loadAllReports() throws SQLException {
        List<ProblemReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM reports";
        try (Connection connection = DatabaseConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    reports.add(new ProblemReport(
                            resultSet.getInt("id"),
                            resultSet.getString("resident_id"),
                            resultSet.getString("location"),
                            ProblemType.valueOf(resultSet.getString("type")),
                            resultSet.getInt("severity"),
                            resultSet.getInt("households"),
                            LocalDateTime.parse(resultSet.getString("reported_at")),
                            Status.valueOf(resultSet.getString("status")),
                            resultSet.getDouble("priority")));
                }
            }
            for (ProblemReport report : reports) {
                loadAssignment(connection, report);
            }
        }
        return reports;
    }

    private void saveAssignment(Connection connection, ProblemReport report) throws SQLException {
        RepairAssignment assignment = report.getAssignment();
        if (assignment == null) {
            return;
        }

        String assignmentSql = "INSERT INTO assignments(report_id,worker_id,assigned_at) VALUES(?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(assignmentSql)) {
            statement.setInt(1, report.getReportId());
            statement.setString(2, assignment.getWorkerId());
            statement.setString(3, assignment.getAssignedAt().toString());
            statement.executeUpdate();
        }

        String recordSql = "INSERT INTO records(report_id,note,date) VALUES(?,?,?)";
        for (MaintenanceRecord record : assignment.getRecords()) {
            try (PreparedStatement statement = connection.prepareStatement(recordSql)) {
                statement.setInt(1, report.getReportId());
                statement.setString(2, record.getNote());
                statement.setString(3, record.getDate().toString());
                statement.executeUpdate();
            }
        }
    }

    private void deleteAssignment(Connection connection, int reportId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM assignments WHERE report_id = ?")) {
            statement.setInt(1, reportId);
            statement.executeUpdate();
        }
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM records WHERE report_id = ?")) {
            statement.setInt(1, reportId);
            statement.executeUpdate();
        }
    }

    private void loadAssignment(Connection connection, ProblemReport report) throws SQLException {
        RepairAssignment assignment = null;

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT worker_id, assigned_at FROM assignments WHERE report_id = ?")) {
            statement.setInt(1, report.getReportId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    assignment = new RepairAssignment(
                            resultSet.getString("worker_id"),
                            LocalDateTime.parse(resultSet.getString("assigned_at")));
                }
            }
        }

        if (assignment == null) {
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT note, date FROM records WHERE report_id = ? ORDER BY rowid")) {
            statement.setInt(1, report.getReportId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    assignment.addRecord(new MaintenanceRecord(
                            resultSet.getString("note"),
                            LocalDateTime.parse(resultSet.getString("date"))));
                }
            }
        }

        report.setAssignment(assignment);
    }
}
