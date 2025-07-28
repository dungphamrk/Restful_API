package ra.ss14.repository;

import ra.ss14.model.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowTimeRepo extends JpaRepository<ShowTime, Long>{
    boolean existsByMovieId(Long movieId);
}
