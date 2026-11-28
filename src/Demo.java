import model.ProblemReport;
import model.ProblemType;
import model.RepairWorker;
import service.PriorityCalculator;
import service.ReportService;
import service.WorkerAssigner;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        ReportService reports = new ReportService();
        PriorityCalculator calculator = new PriorityCalculator();
        WorkerAssigner assigner = new WorkerAssigner();

        assigner.addWorker(new RepairWorker("W001", "Kebede", "0911000003", "work", "plumber"));
        assigner.addWorker(new RepairWorker("W002", "Almaz", "0911000004", "work", "technician"));

        reports.submitReport("R001", "Bole", ProblemType.LOW_PRESSURE, 2, 5);
        reports.submitReport("R002", "Piassa", ProblemType.LEAKAGE, 5, 50);
        reports.submitReport("R003", "Megenagna", ProblemType.NO_WATER, 4, 20);

        System.out.println("=== Reports ranked by priority ===");
        List<ProblemReport> ranked = calculator.getPrioritized(reports.getAllReports());
        for (ProblemReport r : ranked) {
            System.out.printf("Report %d | %s | %s | score %.1f | %s%n",
                    r.getReportId(), r.getLocation(), r.getType(),
                    r.getPriorityScore(), r.getStatus());
        }

        System.out.println();
        System.out.println("=== Assigning a worker to the most urgent report ===");
        ProblemReport top = ranked.get(0);
        List<RepairWorker> suggested = assigner.suggestWorkers(top);
        if (suggested.isEmpty()) {
            System.out.println("No available worker for report " + top.getReportId());
        } else {
            RepairWorker worker = suggested.get(0);
            assigner.assign(top, worker);
            System.out.println("Report " + top.getReportId() + " assigned to "
                    + worker.getName() + " | status: " + top.getStatus());
        }
    }
}