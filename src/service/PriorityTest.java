package service;

import model.ProblemReport;
import model.ProblemType;

import java.util.ArrayList;
import java.util.List;

// Run this file to check the rule: a 50-household leak must outrank a small low-pressure report.
public class PriorityTest {

    public static void main(String[] args) {
        ProblemReport smallReport = new ProblemReport(1, "R1", "Kebele 02", ProblemType.LOW_PRESSURE, 2, 3);
        ProblemReport bigLeak = new ProblemReport(2, "R2", "Kebele 05", ProblemType.LEAKAGE, 5, 50);

        List<ProblemReport> reports = new ArrayList<>();
        reports.add(smallReport);
        reports.add(bigLeak);

        PriorityCalculator calculator = new PriorityCalculator();
        List<ProblemReport> sorted = calculator.getPrioritized(reports);

        for (ProblemReport r : sorted) {
            System.out.println("Report " + r.getReportId() + " score " + r.getPriorityScore());
        }

        if (sorted.get(0).getReportId() == 2) {
            System.out.println("PASS: the big leak is first.");
        } else {
            System.out.println("FAIL: the big leak should be first.");
        }
    }
}
