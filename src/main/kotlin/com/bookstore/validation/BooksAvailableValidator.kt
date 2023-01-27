package com.bookstore.validation

import com.bookstore.service.BookService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class BooksAvailableValidator(
    private var bookService: BookService
): ConstraintValidator<BooksAvailable, Set<Int>> {
    override fun isValid(value: Set<Int>?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrEmpty()) {
            return false
        }
        return bookService.booksAvailable(value)
    }
}
