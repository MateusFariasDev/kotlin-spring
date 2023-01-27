package com.bookstore.controller.response

import com.bookstore.enums.CustomerStatus

data class CustomerResponse (
    var id: Int? = null,
    var name: String,
    var email: String,
    var status: CustomerStatus
)
