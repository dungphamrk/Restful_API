package ra.ss4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss4.entity.Book;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitleContainingIgnoreCase(String title);
}