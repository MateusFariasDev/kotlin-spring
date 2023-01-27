package com.bookstore.exception

class NotFoundException(override val message: String, val errorCode: String): Exception() {
}