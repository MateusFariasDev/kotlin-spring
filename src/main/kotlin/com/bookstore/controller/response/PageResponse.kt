package com.bookstore.controller.response

data class PageResponse<T>(
    var items: List<T>,
    var currentPage: Int,
    var totalPage: Long,
    var totalItems: Int
)