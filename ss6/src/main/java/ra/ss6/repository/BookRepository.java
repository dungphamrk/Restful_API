package ra.ss6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.ss6.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

}
