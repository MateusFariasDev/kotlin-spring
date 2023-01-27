package com.bookstore.enums

enum class Errors(val code: String, val message: String) {
    BS0000("BS-0000", "Access denied!"),
    BS0001("BS-0001", "Invalid request!"),
    BS1001("BS-1001", "Book id %s not exists!"),
    BS1002("BS-1002", "Cannot update book with status %s!"),
    BS1101("BS-1101", "Customer id %s not exists!")
}