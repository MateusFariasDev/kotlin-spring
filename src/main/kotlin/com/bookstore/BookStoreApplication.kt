package com.bookstore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class BookStoreApplication

fun main(args: Array<String>) {
	runApplication<BookStoreApplication>(*args)
}
