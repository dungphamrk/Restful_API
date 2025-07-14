package ra.ss4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.ss4.entity.Book;
import ra.ss4.service.BookService;
import ra.ss4.service.CategoryService;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listBooks(Model model, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("books", bookService.searchByTitle(search));
        } else {
            model.addAttribute("books", bookService.findAll());
        }
        return "book/book-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.findAll());
        return "book/create-book";
    }

    @PostMapping("/create")
    public String createBook(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Book book = bookService.findById(id).orElseThrow();
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryService.findAll());
        return "book/edit-book";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Integer id, @ModelAttribute Book book) {
        book.setId(id);
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Integer id) {
        bookService.delete(id);
        return "redirect:/books";
    }
}