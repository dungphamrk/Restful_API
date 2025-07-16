package ra.ss6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ra.ss6.model.Book;
import ra.ss6.model.DataResponse;
import ra.ss6.repository.BookRepository;
import ra.ss6.service.BookService;

import java.util.List;

@Controller
@RequestMapping("books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<DataResponse<List<Book>>> getAllBooks()  {
        return new ResponseEntity<>(new DataResponse<>(bookService.findAllBooks(),HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public  ResponseEntity<DataResponse<Book>> findById(@PathVariable Long bookId){
        return new ResponseEntity<>(new DataResponse<>(bookService.findBookById(bookId),HttpStatus.OK),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DataResponse<Book>> saveBook(@RequestBody Book book){
        return new ResponseEntity<>(new DataResponse<>(bookService.createBook(book),HttpStatus.OK),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<DataResponse<Book>> updateBook(@RequestBody Book book){
        return new ResponseEntity<>(new DataResponse<>(bookService.updateBook(book),HttpStatus.OK),HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId){
        return new ResponseEntity<>(new DataResponse<>(bookService.deleteBook(bookId),HttpStatus.NO_CONTENT),HttpStatus.NO_CONTENT);
    }


}
