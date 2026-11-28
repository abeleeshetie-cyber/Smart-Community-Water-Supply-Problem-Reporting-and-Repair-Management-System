package model;

import java.time.LocalDateTime;

public class ProblemReport {
    private final int reportId;
    private final String residentId;
    private final String location;
    private final ProblemType type;
    private final int severity;
    private final int affectedHouseholds;
    private final LocalDateTime reportedAt;
    private Status status;
    private double priorityScore;
    private RepairAssignment assignment;

    // New report: dated now, status SUBMITTED
    public ProblemReport(int reportId, String residentId, String location,
                         ProblemType type, int severity, int affectedHouseholds) {
        this(reportId, residentId, location, type, severity, affectedHouseholds,
                LocalDateTime.now(), Status.SUBMITTED, 0.0);
    }

    // For loading from the database
    public ProblemReport(int reportId, String residentId, String location,
                         ProblemType type, int severity, int affectedHouseholds,
                         LocalDateTime reportedAt, Status status, double priorityScore) {
        this.reportId = reportId;
        this.residentId = residentId;
        this.location = location;
        this.type = type;
        this.severity = severity;
        this.affectedHouseholds = affectedHouseholds;
        this.reportedAt = reportedAt;
        this.status = status;
        this.priorityScore = priorityScore;
        this.assignment = null;
    }

    public int getReportId() { return reportId; }
    public String getResidentId() { return residentId; }
    public String getLocation() { return location; }
    public ProblemType getType() { return type; }
    public int getSeverity() { return severity; }
    public int getAffectedHouseholds() { return affectedHouseholds; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public Status getStatus() { return status; }
    public double getPriorityScore() { return priorityScore; }
    public void setPriorityScore(double score) { this.priorityScore = score; }

    // null until a worker is assigned
    public RepairAssignment getAssignment() { return assignment; }
    public void setAssignment(RepairAssignment a) { this.assignment = a; }

    // The only way to change the status
    public void updateStatus(Status newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return "Report #" + reportId + " [" + type + ", severity " + severity
                + ", " + location + ", " + status + "]";
    }
}
