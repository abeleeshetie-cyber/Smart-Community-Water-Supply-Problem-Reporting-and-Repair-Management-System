package service;

import model.ProblemReport;
import model.ProblemType;
import model.RepairAssignment;
import model.RepairWorker;
import model.Status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkerAssigner {
    private List<RepairWorker> workers = new ArrayList<>();
    private Map<String, List<RepairWorker>> workersByTrade = new HashMap<>();

    public void addWorker(RepairWorker worker) {
        workers.add(worker);
        String trade = worker.getTrade().toLowerCase();
        workersByTrade.computeIfAbsent(trade, k -> new ArrayList<>()).add(worker);
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
        List<RepairWorker> sameTrade =
                workersByTrade.getOrDefault(trade, new ArrayList<>());
        for (RepairWorker w : sameTrade) {
            if (w.isAvailable()) {
                result.add(w);
            }
        }
        return result;
    }

    public boolean hasAvailableWorker(ProblemReport report) {
        return !suggestWorkers(report).isEmpty();
    }

    public boolean assign(ProblemReport report, RepairWorker worker) {
        if (worker == null || !worker.isAvailable()) {
            return false;
        }
        RepairAssignment assignment =
                new RepairAssignment(worker.getId(), LocalDateTime.now());
        report.setAssignment(assignment);
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
