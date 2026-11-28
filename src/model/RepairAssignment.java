package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepairAssignment {

    private final String workerId;
    private final LocalDateTime assignedAt;
    private final List<MaintenanceRecord> records;

    // dated now
    public RepairAssignment(String workerId) {
        this(workerId, LocalDateTime.now());
    }

    public RepairAssignment(String workerId, LocalDateTime assignedAt) {
        this.workerId = workerId;
        this.assignedAt = assignedAt;
        this.records = new ArrayList<>();
    }

    public String getWorkerId() {
        return workerId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void addRecord(MaintenanceRecord r) {
        records.add(r);
    }

    public List<MaintenanceRecord> getRecords() {
        return records;
    }
}
