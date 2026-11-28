package model;

import java.time.LocalDateTime;

public class MaintenanceRecord {

    private final String note;
    private final LocalDateTime date;

    // dated now
    public MaintenanceRecord(String note) {
        this(note, LocalDateTime.now());
    }

    public MaintenanceRecord(String note, LocalDateTime date) {
        this.note = note;
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
