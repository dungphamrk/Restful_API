package ra.ss7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ra.ss7.model.Harvest;
import ra.ss7.model.dto.DataResponse;
import ra.ss7.service.HarvestService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/harvests")
public class HarvestController {

    @Autowired
    private HarvestService harvestService;

    // GET /harvests: Lấy danh sách tất cả thu hoạch
    @GetMapping
    public ResponseEntity<DataResponse<List<Harvest>>> getHarvests() {
        List<Harvest> harvests = harvestService.getAllHarvests();
        return new ResponseEntity<>(new DataResponse<>(harvests, HttpStatus.OK), HttpStatus.OK);
    }

    // POST /harvests: Thêm thông tin thu hoạch mới
    @PostMapping
    public ResponseEntity<DataResponse<Harvest>> addHarvest(@Valid @RequestBody Harvest harvest) {
        Harvest savedHarvest = harvestService.addHarvest(harvest);
        return new ResponseEntity<>(new DataResponse<>(savedHarvest, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    // GET /harvests/{id}: Lấy thông tin thu hoạch theo id
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<?>> getHarvestById(@PathVariable Long id) {
        try {
            Harvest harvest = harvestService.getHarvestById(id);
            return new ResponseEntity<>(new DataResponse<>(harvest, HttpStatus.OK), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new DataResponse<>(e.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }
}