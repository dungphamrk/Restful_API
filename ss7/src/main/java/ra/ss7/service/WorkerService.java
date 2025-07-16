package ra.ss7.service;

import ra.ss7.model.Worker;

import java.util.List;

public interface WorkerService {
    List<Worker> getAllWorkers();
    Worker getWorkerById(Long id);
    Worker addWorker(Worker worker);
    Worker updateWorker(Long id, Worker worker);
    void deleteWorker(Long id);
}