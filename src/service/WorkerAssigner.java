package service;

import model.ProblemReport;
import model.ProblemType;
import model.RepairWorker;
import model.Status;

import java.util.ArrayList;
import java.util.List;

public class WorkerAssigner {

    private List<RepairWorker> workers = new ArrayList<>();

    public void addWorker(RepairWorker worker) {
        workers.add(worker);
    }

    private String tradeFor(ProblemType type) {
        return switch (type) {
            case LEAKAGE -> "plumber";
            case NO_WATER -> "technician";
            case LOW_PRESSURE -> "technician";
        };
    }

    public List<RepairWorker> suggestWorkers(ProblemReport report) {
        String trade = tradeFor(report.getType());
        List<RepairWorker> result = new ArrayList<>();
        for (RepairWorker w : workers) {
            if (w.getTrade().equalsIgnoreCase(trade) && w.isAvailable()) {
                result.add(w);
            }
        }
        return result;
    }

    public boolean assign(ProblemReport report, RepairWorker worker) {
        if (!worker.isAvailable()) {
            return false;
        }
        worker.setAvailable(false);
        report.updateStatus(Status.ASSIGNED);
        return true;
    }

    public void startRepair(ProblemReport report) {
        report.updateStatus(Status.IN_PROGRESS);
    }

    public void completeRepair(ProblemReport report, RepairWorker worker) {
        report.updateStatus(Status.RESOLVED);
        worker.setAvailable(true);
    }
}
