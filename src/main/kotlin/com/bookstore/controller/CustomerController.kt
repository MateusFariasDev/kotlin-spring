package com.bookstore.controller

import com.bookstore.controller.request.PostCustomerRequest
import com.bookstore.controller.request.PutCustomerRequest
import com.bookstore.controller.response.CustomerResponse
import com.bookstore.extension.toCustomerModel
import com.bookstore.extension.toResponse
import com.bookstore.security.UserCanOnlyAccessTheirOwnResource
import com.bookstore.service.CustomerService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customers")
class CustomerController(
    val customerService : CustomerService
) {

    @GetMapping
    fun getAll(@PageableDefault(page = 0, size = 10) pageable: Pageable, @RequestParam name: String?): Page<CustomerResponse> {
        return customerService.getAll(pageable, name).map { it.toResponse() }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid customer: PostCustomerRequest) {
        customerService.create(customer.toCustomerModel())
    }

    @GetMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    fun getById(@PathVariable id: Int): CustomerResponse? {
        return customerService.findById(id)?.toResponse()
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @UserCanOnlyAccessTheirOwnResource
    fun update(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
        val customerSaved = customerService.findById(id)

        customerService.update(customer.toCustomerModel(customerSaved!!))
    }

    @DeleteMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        customerService.delete(id)
    }
}