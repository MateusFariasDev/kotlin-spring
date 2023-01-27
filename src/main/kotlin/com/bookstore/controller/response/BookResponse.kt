package com.bookstore.controller.response

import com.bookstore.enums.BookStatus
import com.bookstore.model.CustomerModel
import java.math.BigDecimal

data class BookResponse (
    var id: Int? = null,
    var name: String,
    var price: BigDecimal,
    var status: BookStatus? = null,
    var customer: CustomerModel? = null
)
