package model;

import java.time.LocalDateTime;

public class MaintenanceRecord {
    private String note;
    private LocalDateTime date;

    public MaintenanceRecord(String note, LocalDateTime date) {
        this.note = note;
        this.date = date;
    }

    // new record dated now
    public MaintenanceRecord(String note) {
        this(note, LocalDateTime.now());
    }

    public String getNote() { return note; }
    public LocalDateTime getDate() { return date; }
}
