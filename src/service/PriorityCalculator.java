package service;

import model.ProblemReport;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PriorityCalculator {

    private static final double SEVERITY_WEIGHT = 3;
    private static final double HOUSEHOLD_WEIGHT = 0.5;
    private static final double DAYS_OPEN_WEIGHT = 2;

    public PriorityCalculator() {
    }

    // score = (severity x 3) + (households x 0.5) + (daysOpen x 2)
    public double calculate(ProblemReport r) {
        long daysOpen = ChronoUnit.DAYS.between(r.getReportedAt(), LocalDateTime.now());
        return (r.getSeverity() * SEVERITY_WEIGHT)
                + (r.getAffectedHouseholds() * HOUSEHOLD_WEIGHT)
                + (daysOpen * DAYS_OPEN_WEIGHT);
    }

    // most urgent first. Works on a copy, so the original list keeps its order.
    public List<ProblemReport> getPrioritized(List<ProblemReport> reports) {
        List<ProblemReport> sorted = new ArrayList<>(reports);
        for (ProblemReport r : sorted) {
            r.setPriorityScore(calculate(r));
        }
        sorted.sort((a, b) -> Double.compare(b.getPriorityScore(), a.getPriorityScore()));
        return sorted;
    }
}
