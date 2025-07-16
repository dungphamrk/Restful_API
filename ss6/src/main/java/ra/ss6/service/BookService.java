package ra.ss6.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss6.model.Book;
import ra.ss6.repository.BookRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookService {
    @Autowired
    public BookRepository bookRepository;

    public Book createBook(Book newBook){
       return bookRepository.save(newBook);
    }
    public Book updateBook(Book newBook){
        bookRepository.findById(newBook.getId()).orElseThrow(()-> new NoSuchElementException("Khong ton tai book!"));
        return bookRepository.save(newBook);
    }
    public boolean deleteBook(Long id){
        bookRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Khong ton tai book!"));
        bookRepository.deleteById(id);
        return true;
    }
    public Book findBookById(Long id){
        return bookRepository.findById(id).orElse(null);
    }
    public List<Book> findAllBooks(){
        return bookRepository.findAll();
    }
}
