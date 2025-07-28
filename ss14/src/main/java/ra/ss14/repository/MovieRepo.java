package ra.ss14.repository;

import ra.ss14.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepo extends JpaRepository<Movie, Long>{
    boolean existsByTitle(String title);
}
