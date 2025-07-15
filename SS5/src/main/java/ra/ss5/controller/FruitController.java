package ra.ss5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.ss5.entity.Fruit;
import ra.ss5.model.bt10.DataResponse;
import ra.ss5.service.FruitService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fruits")
public class FruitController {

    @Autowired
    private FruitService fruitService;

    @GetMapping
    public ResponseEntity<DataResponse<List<Fruit>>> getAllFruits() {
        return new ResponseEntity<>(
                new DataResponse<>(fruitService.getAllFruits(), HttpStatus.OK),
                HttpStatus.OK
        );
    }

    @GetMapping("/{fruitId}")
    public ResponseEntity<DataResponse<Fruit>> getFruitById(@PathVariable Long fruitId) {
        return new ResponseEntity<>(
                new DataResponse<>(fruitService.getFruitById(fruitId), HttpStatus.OK),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<DataResponse<Fruit>> createFruit(@RequestBody Fruit fruit) {
        return new ResponseEntity<>(
                new DataResponse<>(fruitService.createFruit(fruit), HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<DataResponse<Fruit>> updateFruit(@RequestBody Fruit fruit) {
        return new ResponseEntity<>(
                new DataResponse<>(fruitService.updateFruit(fruit.getId(), fruit), HttpStatus.OK),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{fruitId}")
    public ResponseEntity<?> deleteFruit(@PathVariable Long fruitId) {
        fruitService.deleteFruit(fruitId);
        return new ResponseEntity<>(
                new DataResponse<>(null, HttpStatus.NO_CONTENT, "Xóa hoa quả thành công"),
                HttpStatus.NO_CONTENT
        );
    }

    @GetMapping(value = "/fruits-by-name/{fruitName}", produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<DataResponse<List<Fruit>>> getFruitsByName(@PathVariable String fruitName) {
        return new ResponseEntity<>(
                new DataResponse<>(fruitService.getFruitsByName(fruitName), HttpStatus.OK),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/fruits-by-price-range", produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<DataResponse<List<Fruit>>> getFruitsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        return new ResponseEntity<>(
                new DataResponse<>(fruitService.getFruitsByPriceRange(minPrice, maxPrice), HttpStatus.OK),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/fruits-by-status/{status}", produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<DataResponse<List<Fruit>>> getFruitsByStatus(@PathVariable Boolean status) {
        return new ResponseEntity<>(
                new DataResponse<>(fruitService.getFruitsByStatus(status), HttpStatus.OK),
                HttpStatus.OK
        );
    }
}
