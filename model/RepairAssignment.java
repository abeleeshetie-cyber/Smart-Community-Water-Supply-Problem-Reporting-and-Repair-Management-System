package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepairAssignment {
    private String workerId;
    private LocalDateTime assignedAt;
    private List<MaintenanceRecord> records = new ArrayList<>();

    public RepairAssignment(String workerId, LocalDateTime assignedAt) {
        this.workerId = workerId;
        this.assignedAt = assignedAt;
    }

    // new assignment dated now
    public RepairAssignment(String workerId) {
        this(workerId, LocalDateTime.now());
    }

    public String getWorkerId() { return workerId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }

    public void addRecord(MaintenanceRecord record) {
        records.add(record);
    }

    public List<MaintenanceRecord> getRecords() {
        return new ArrayList<>(records);
    }
}
