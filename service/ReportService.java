package service;

import model.ProblemReport;
import model.ProblemType;

import java.util.ArrayList;
import java.util.List;

public class ReportService {
    // ArrayList keeps submission order and is simple to loop over
    private final List<ProblemReport> reports = new ArrayList<>();
    private int nextId = 1;

    public ReportService() {
    }

    public ProblemReport submitReport(String residentId, String location,
                                      ProblemType type, int severity, int households) {
        if (residentId == null || residentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Resident ID is required");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Problem type is required");
        }
        if (severity < 1 || severity > 5) {
            throw new IllegalArgumentException("Severity must be between 1 and 5");
        }
        if (households < 1) {
            throw new IllegalArgumentException("Households must be at least 1");
        }

        // TODO (still to be implemented): duplicate check goes here,
        // e.g. DuplicateDetector.isDuplicate(newReport, reports) by location + type

        ProblemReport report = new ProblemReport(nextId++, residentId, location.trim(),
                type, severity, households);
        reports.add(report);
        return report;
    }

    // Used when reports are loaded from the database
    public void addReport(ProblemReport report) {
        reports.add(report);
        if (report.getReportId() >= nextId) {
            nextId = report.getReportId() + 1;
        }
    }

    public List<ProblemReport> getAllReports() {
        return new ArrayList<>(reports);
    }

    public List<ProblemReport> getReportsByResident(String residentId) {
        List<ProblemReport> result = new ArrayList<>();
        for (ProblemReport r : reports) {
            if (r.getResidentId().equals(residentId)) {
                result.add(r);
            }
        }
        return result;
    }

    public ProblemReport findById(int id) {
        for (ProblemReport r : reports) {
            if (r.getReportId() == id) {
                return r;
            }
        }
        return null;
    }
}
