package model;

import java.time.LocalDateTime;

public class ProblemReport {
    private int reportId;
    private String residentId;
    private String location;
    private ProblemType type;
    private int severity;            // 1 to 5
    private int affectedHouseholds;
    private LocalDateTime reportedAt;
    private Status status;
    private double priorityScore;
    private RepairAssignment assignment;   // null until a worker is assigned

    // new report: dated now, status SUBMITTED
    public ProblemReport(int reportId, String residentId, String location,
                         ProblemType type, int severity, int affectedHouseholds) {
        this(reportId, residentId, location, type, severity, affectedHouseholds,
             LocalDateTime.now(), Status.SUBMITTED, 0);
    }

    // used by the database layer when loading a saved report
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

    public RepairAssignment getAssignment() { return assignment; }
    public void setAssignment(RepairAssignment assignment) { this.assignment = assignment; }

    // The only way to change status
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }
}
