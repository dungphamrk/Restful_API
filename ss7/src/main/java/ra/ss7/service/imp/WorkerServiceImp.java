package ra.ss7.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss7.model.Worker;
import ra.ss7.repository.WorkerRepository;
import ra.ss7.service.WorkerService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WorkerServiceImp implements WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    @Override
    public Worker getWorkerById(Long id) {
        return workerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy công nhân có id: " + id));
    }

    @Override
    public Worker addWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    @Override
    public Worker updateWorker(Long id, Worker worker) {
        workerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy công nhân có id: " + id));
        worker.setId(id);
        return workerRepository.save(worker);
    }

    @Override
    public void deleteWorker(Long id) {
        workerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy công nhân có id: " + id));
        workerRepository.deleteById(id);
    }
}