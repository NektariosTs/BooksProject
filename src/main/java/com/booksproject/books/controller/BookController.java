package com.booksproject.books.controller;

import com.booksproject.books.entity.Book;
import com.booksproject.books.request.BookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Books Rest API Endpoints", description = "Operations related to books")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();

    public BookController() {
        initializeBooks();
    }

    private void initializeBooks() {
        books.addAll(List.of(
                new Book(1, "Computer Science Pro", "Chad Darby", "Computer Science", 5),
                new Book(2, "Java Spring Master", "Eric Roby", "Computer Science", 5),
                new Book(3, "Why 1+1 Rocks", "Adil A.", "Math", 5),
                new Book(4, "How Bears Hibernate", "Bob B.", "Science", 2),
                new Book(5, "A Pirate's Treasure", "Curt C.", "History", 3),
                new Book(6, "Why 2+2 is Better", "Dan D.", "Math", 1)
        ));
    }


    @Operation(summary = "Get all books", description = "Retrieve a list of all available books")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Book> getBooks(@Parameter(description = "Optional query parameter") @RequestParam(required = false) String category) {

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


    @Operation(summary = "Geta book by Id", description = "Retrieve a specific book by Id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Book getBookById(@Parameter(description = "Id of book to be retrieved") @PathVariable @Min(value = 1) Long id) {

//        if (id < 1) { Validation
//            throw new Exception("incorrect id");
//        }
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
//        for (Book book : books) {
//            if (book.getTitle().equalsIgnoreCase(title)){
//                return book;
//            }
//        }
//        return null;

    }


    @Operation(summary = "Create a new book", description = "Add a new book to the list")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createBook(@Valid @RequestBody BookRequest bookRequest) {
//        long id;
//        if (books.isEmpty()) {
//            id = 1;
//        } else {
//            id = books.get(books.size() - 1).getId() + 1;
//        }
        long id = books.isEmpty() ? 1 : books.get(books.size() - 1).getId() + 1; //ternary operator

        Book book = convertToBook(id, bookRequest);
//        Book book = new Book(
//                id,
//                bookRequest.getTitle(),
//                bookRequest.getAuthor(),
//                bookRequest.getCategory(),
//                bookRequest.getRating()
//        )
        books.add(book);
//        for (Book book : books) {
//            if (book.getTitle().equalsIgnoreCase(newBook.getTitle())){
//                return;
//            }
//        }
//        books.add(newBook);
    }


    @Operation(summary = "Update a book", description = "Update the details of an existing book")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateBook(@Parameter(description = "Id of the book to update") @PathVariable @Min(value = 1) long id, @Valid @RequestBody BookRequest bookRequest) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                Book updatedBook = convertToBook(id, bookRequest);
                books.set(i, updatedBook);
                return;
            }
        }
    }


    @Operation(summary = "Delete a book", description = "Remove a book from a list")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBook(@Parameter(description = "Remove a book from the list") @PathVariable @Min(value = 1) long id) {
        books.removeIf(book -> book.getId() == id);
    }

    private Book convertToBook(long id, BookRequest bookRequest) {
        return new Book(
                id,
                bookRequest.getTitle(),
                bookRequest.getAuthor(),
                bookRequest.getCategory(),
                bookRequest.getRating()
        );
    }
}
