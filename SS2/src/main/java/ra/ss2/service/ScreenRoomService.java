package ra.ss2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss2.entity.ScreenRoom;
import ra.ss2.repository.ScreenRoomRepository;

import java.util.List;

@Service
public class ScreenRoomService implements IService<ScreenRoom, Long> {

    @Autowired
    private ScreenRoomRepository screenRoomRepository;


    @Override
    public List<ScreenRoom> findAll() {
        return screenRoomRepository.findAll();
    }

    @Override
    public void save(ScreenRoom screenRoom) {
        screenRoomRepository.save(screenRoom);
    }

    @Override
    public ScreenRoom findById(Long id) {
        return screenRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ScreenRoom not found with id: " + id));
    }

    @Override
    public ScreenRoom update(ScreenRoom screenRoom) {
        if (!screenRoomRepository.existsById(screenRoom.getId())) {
            throw new RuntimeException("ScreenRoom not found with id: " + screenRoom.getId());
        }
        return screenRoomRepository.save(screenRoom);
    }

    @Override
    public void delete(Long id) {
        if (!screenRoomRepository.existsById(id)) {
            throw new RuntimeException("ScreenRoom not found with id: " + id);
        }
        screenRoomRepository.deleteById(id);
    }

    private List<ScreenRoom> findByNameContainingIgnoreCase(String name){
        return screenRoomRepository.findByNameContainingIgnoreCase(name);
    }

    private List<ScreenRoom> findByTheaterId(Long theaterId){
        return screenRoomRepository.findByTheaterId(theaterId);
    }
}