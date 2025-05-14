package com.booksproject.books.controller;

import com.booksproject.books.entity.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();

    public BookController() {
        initializeBooks();
    }

    private void initializeBooks() {
        books.addAll(List.of(
                new Book("Title one", "Author one", "science"),
                new Book("Title two", "Author two", "science"),
                new Book("Title three", "Author three", "history"),
                new Book("Title four", "Author four", "math"),
                new Book("Title five", "Author five", "math"),
                new Book("Title six", "Author six", "math")
        ));
    }

    @GetMapping
    public List<Book> getBooks(@RequestParam(required = false) String category) {

        if (category == null) {
            return books;
        }

        return books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();
        //better implementation

//        List<Book> filteredBooks = new ArrayList<>();
//
//        for (Book book : books) {
//            if (book.getCategory().equalsIgnoreCase(category)) {
//                filteredBooks.add(book);
//            }
//        }
//        return filteredBooks;
    }

    @GetMapping("/{title}")
    public Book getBookByTitle(@PathVariable String title) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
//        for (Book book : books) {
//            if (book.getTitle().equalsIgnoreCase(title)){
//                return book;
//            }
//        }
//        return null;

    }

    @PostMapping
    public void createBook(@RequestBody Book newBook) {

        boolean isNewBook = books.stream()
                .noneMatch(book -> book.getTitle().equalsIgnoreCase(newBook.getTitle()));
        if (isNewBook) {
            books.add(newBook);
        }
//        for (Book book : books) {
//            if (book.getTitle().equalsIgnoreCase(newBook.getTitle())){
//                return;
//            }
//        }
//        books.add(newBook);
    }

    @PutMapping("/{title}")
    public void updateBook(@PathVariable String title, @RequestBody Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equalsIgnoreCase(title)) {
                books.set(i, updatedBook);
                return;
            }
        }
    }

    @DeleteMapping("/{title}")
    public void deleteBook(@PathVariable String title) {
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
    }
}
