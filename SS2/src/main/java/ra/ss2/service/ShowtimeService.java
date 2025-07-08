package ra.ss2.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.Show;
import org.springframework.stereotype.Service;
import ra.ss2.entity.Showtime;
import ra.ss2.repository.ShowtimeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeService implements IService<Showtime, Long> {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Override
    public List<Showtime> findAll(){
        return showtimeRepository.findAll();
    }

    @Override
    public void save(Showtime showtime) {
        showtimeRepository.save(showtime);
    }

    @Override
    public Showtime findById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("showtime not found with id: " + id));
    }

    public List<Showtime> filterShowtimes(Long movieId, String date, Long cinemaId, Long roomId) {

        List<Showtime> listShowtime = new ArrayList<>();
        listShowtime=showtimeRepository.findAll().stream()
                .filter(s -> movieId == null || s.getMovie().getId().equals(movieId))
                .filter(s -> date == null || s.getStartTime().toLocalDate().toString().equals(date))
                .filter(s -> cinemaId == null || s.getScreenRoom().getTheater().getId().equals(cinemaId))
                .filter(s -> roomId == null || s.getScreenRoom().getId().equals(roomId))
                .collect(Collectors.toList());
        return listShowtime;

    }

    @Override
    public Showtime update(Showtime showtime) {
        if (!showtimeRepository.existsById(showtime.getId())) {
            throw new RuntimeException("showtime not found with id: " + showtime.getId());
        }
        return showtimeRepository.save(showtime);
    }

    @Override
    public void delete(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new RuntimeException("showtime not found with id: " + id);
        }
        showtimeRepository.deleteById(id);
    }

    private List<Showtime> findByMovieId(Long movieId){
        return showtimeRepository.findByMovieId(movieId);
    }
    private List<Showtime> findByScreenRoomId(Long screenRoomId){
        return showtimeRepository.findByScreenRoomId(screenRoomId);
    }
}
