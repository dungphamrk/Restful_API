package ra.ss2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss2.entity.Theater;
import ra.ss2.repository.TheaterRepository;

import java.util.List;

@Service
public class TheaterService implements IService<Theater, Long> {

    @Autowired
    private TheaterRepository theaterRepository;

    @Override
    public List<Theater> findAll(){
        return theaterRepository.findAll();
    }

    @Override
    public void save(Theater theater) {
        theaterRepository.save(theater);
    }

    @Override
    public Theater findById(Long id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theater not found with id: " + id));
    }

    @Override
    public Theater update(Theater theater) {
        if (!theaterRepository.existsById(theater.getId())) {
            throw new RuntimeException("Theater not found with id: " + theater.getId());
        }
        return theaterRepository.save(theater);
    }

    @Override
    public void delete(Long id) {
        if (!theaterRepository.existsById(id)) {
            throw new RuntimeException("Theater not found with id: " + id);
        }
        theaterRepository.deleteById(id);
    }

    private List<Theater> findByNameContainingIgnoreCase(String name){
        return theaterRepository.findByNameContainingIgnoreCase(name);
    }

    private List<Theater> findByAddressContainingIgnoreCase(String address){
        return theaterRepository.findByAddressContainingIgnoreCase(address);
    }
}