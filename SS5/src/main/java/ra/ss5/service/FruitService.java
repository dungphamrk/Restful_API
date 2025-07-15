package ra.ss5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss5.entity.Fruit;
import ra.ss5.repository.FruitRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class FruitService {

    @Autowired
    private FruitRepository fruitRepository;

    public List<Fruit> getAllFruits() {
        return fruitRepository.findAll();
    }

    public Fruit getFruitById(Long id) {
        return fruitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hoa quả với ID: " + id));
    }

    public Fruit createFruit(Fruit fruit) {
        fruit.setCreatedAt(LocalDate.now());
        fruit.setStatus(true);
        return fruitRepository.save(fruit);
    }

    public Fruit updateFruit(Long id, Fruit fruit) {
        Fruit existingFruit = getFruitById(id);

        existingFruit.setName(fruit.getName());
        existingFruit.setPrice(fruit.getPrice());
        existingFruit.setStock(fruit.getStock());
        existingFruit.setStatus(fruit.getStatus());

        return fruitRepository.save(existingFruit);
    }

    public void deleteFruit(Long id) {
        if (!fruitRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy hoa quả với ID: " + id);
        }
        fruitRepository.deleteById(id);
    }

    public List<Fruit> getFruitsByName(String fruitName) {
        return fruitRepository.findByNameContainingIgnoreCase(fruitName);
    }

    public List<Fruit> getFruitsByPriceRange(Double minPrice, Double maxPrice) {
        return fruitRepository.findByPriceRange(minPrice, maxPrice);
    }

    public List<Fruit> getFruitsByStatus(Boolean status) {
        return fruitRepository.findByStatus(status);
    }
}

