package ra.ss2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss2.entity.Showtime;

import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime,Long> {
    List<Showtime> findByMovieId(Long movieId);
    List<Showtime> findByScreenRoomId(Long screenRoomId);
}
