package com.naranov.books.controller;

import com.naranov.books.controller.errors.BookAlreadyExistsException;
import com.naranov.books.controller.errors.BookNotFoundException;
import com.naranov.books.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.naranov.books.repo.BookRepository;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rest")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/book")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/book/{id}")
    public ResponseEntity <Book> getBookById(@PathVariable(value = "id") Long bookId) throws BookNotFoundException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found for this id: " + bookId));
        return ResponseEntity.ok().body(book);
    }

    @PostMapping("/book")
    public Book createBook(@Valid @RequestBody Book book) throws BookAlreadyExistsException{
        Book currentBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        // In this case there is no reason to equals current book and new book.
        if (currentBook != null){
            throw new BookAlreadyExistsException("Book already exists for title: " + currentBook.getTitle() +
                    ", and author: " + currentBook.getAuthor());
        } else {
            return bookRepository.save(book);
        }
    }

    @PutMapping("/book/{id}")
    public ResponseEntity <Book> updateBook(@PathVariable(value = "id") Long bookId, @Valid @RequestBody Book bookDetails) throws BookNotFoundException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found for this id: " + bookId));

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setDescription(bookDetails.getDescription());

        final Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/book/{id}")
    public Map<String, Boolean> deleteBook(@PathVariable(value = "id") Long bookId)
            throws BookNotFoundException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found for this id: " + bookId));

        bookRepository.delete(book);
        Map <String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}