package com.bookstore.service

import com.bookstore.enums.BookStatus
import com.bookstore.enums.Errors
import com.bookstore.exception.NotFoundException
import com.bookstore.model.BookModel
import com.bookstore.model.CustomerModel
import com.bookstore.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository
) {
    fun create(book: BookModel) {
        bookRepository.save(book)
    }

    fun findAll(pageable: Pageable): Page<BookModel> {
        return bookRepository.findAll(pageable)
    }

    fun findActives(pageable: Pageable): Page<BookModel> {
        return bookRepository.findByStatus(BookStatus.ATIVO, pageable)
    }

    fun findById(id: Int): BookModel {
        return bookRepository.findById(id).orElseThrow{ NotFoundException(Errors.BS1001.message.format(id), Errors.BS1001.code) }
    }

    fun delete(id: Int) {
        val book = bookRepository.findById(id).orElseThrow()
        book.status = BookStatus.CANCELADO

        update(book)
    }

    fun update(book: BookModel) {
        bookRepository.save(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        val books = bookRepository.findByCustomer(customer)

        for (book in books) {
            book.status = BookStatus.DELETADO
        }

        bookRepository.saveAll(books)
    }

    fun findAllByIds(booksIds: Set<Int>): List<BookModel> {
        return bookRepository.findAllById(booksIds).toList()
    }

    fun purchase(books: MutableList<BookModel>) {
        books.map {
            it.status = BookStatus.VENDIDO
        }
        bookRepository.saveAll(books)
    }

    fun booksAvailable(value: Set<Int>): Boolean {
        value.map {
            val book = findById(it)

            if (book.status != BookStatus.ATIVO) {
                return false
            }
        }
        return true
    }
}