package com.bookstore.controller.request

import com.bookstore.validation.BooksAvailable
import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class PostPurchaseRequest(
    @field:NotNull
    @field:Positive
    @JsonAlias("customer_id")
    val customerId: Int,

    @JsonAlias("books_ids")
    @BooksAvailable
    val booksIds: Set<Int>
) {

}
