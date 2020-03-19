package com.naranov.books.controller.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BookAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public BookAlreadyExistsException(String message) {
        super(message);
    }
}