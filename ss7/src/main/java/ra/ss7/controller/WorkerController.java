package ra.ss7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ra.ss7.model.Worker;
import ra.ss7.model.dto.DataResponse;
import ra.ss7.service.WorkerService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    // GET /workers: Lấy danh sách tất cả công nhân
    @GetMapping
    public ResponseEntity<DataResponse<List<Worker>>> getWorkers() {
        List<Worker> workers = workerService.getAllWorkers();
        return new ResponseEntity<>(new DataResponse<>(workers, HttpStatus.OK), HttpStatus.OK);
    }

    // POST /workers: Thêm công nhân mới
    @PostMapping
    public ResponseEntity<DataResponse<Worker>> addWorker(@Valid @RequestBody Worker worker) {
        Worker savedWorker = workerService.addWorker(worker);
        return new ResponseEntity<>(new DataResponse<>(savedWorker, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    // PUT /workers/{id}: Cập nhật thông tin công nhân
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<Worker>> updateWorker(@PathVariable Long id, @Valid @RequestBody Worker worker) {
        Worker updatedWorker = workerService.updateWorker(id, worker);
        return new ResponseEntity<>(new DataResponse<>(updatedWorker, HttpStatus.OK), HttpStatus.OK);
    }

    // DELETE /workers/{id}: Xóa công nhân
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<String>> deleteWorker(@PathVariable Long id) {
        try {
            workerService.deleteWorker(id);
            return new ResponseEntity<>(new DataResponse<>("Xóa công nhân thành công", HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new DataResponse<>(e.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }
}