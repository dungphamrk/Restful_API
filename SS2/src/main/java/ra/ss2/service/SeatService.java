package ra.ss2.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss2.entity.Seat;
import ra.ss2.repository.SeatRepository;

import java.util.List;

@Service
public class SeatService implements IService<Seat, Long> {

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public List<Seat> findAll() {
        return seatRepository.findAll();
    }

    @Override
    public void save(Seat seat) {
        seatRepository.save(seat);
    }

    @Override
    public Seat findById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + id));
    }

    @Override
    public Seat update(Seat seat) {
        if (!seatRepository.existsById(seat.getId())) {
            throw new RuntimeException("Seat not found with id: " + seat.getId());
        }
        return seatRepository.save(seat);
    }

    @Override
    public void delete(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new RuntimeException("Seat not found with id: " + id);
        }
        seatRepository.deleteById(id);
    }

    private List<Seat> findBySeatNumber(String seatNumber){
        return seatRepository.findBySeatNumber(seatNumber);
    }
    private List<Seat> findByScreenRoomId(Long screenRoomId){
        return seatRepository.findByScreenRoomId(screenRoomId);
    }
}