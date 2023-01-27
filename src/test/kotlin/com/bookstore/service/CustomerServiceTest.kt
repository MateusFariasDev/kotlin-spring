package com.bookstore.service

import com.bookstore.enums.CustomerStatus
import com.bookstore.enums.Errors
import com.bookstore.enums.Role
import com.bookstore.exception.NotFoundException
import com.bookstore.helper.buildCustomer
import com.bookstore.model.CustomerModel
import com.bookstore.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.Optional
import java.util.UUID
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
class CustomerServiceTest{
    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        val pageable = PageRequest.of(0, 10)
        val fakeCustomers = PageImpl(listOf(buildCustomer(), buildCustomer()), pageable, 2)

        every { customerRepository.findAll(pageable) } returns fakeCustomers

        val customers = customerService.getAll(pageable, null)

        assertEquals(fakeCustomers, customers)
        verify(exactly = 1) { customerRepository.findAll(pageable) }
        verify(exactly = 0) { customerRepository.findByNameContaining(any(), pageable) }
    }

    @Test
    fun `should return all when name is informed`() {
        val name = UUID.randomUUID().toString()
        val pageable = PageRequest.of(0, 10)
        val fakeCustomers = PageImpl(listOf(buildCustomer(), buildCustomer()), pageable, 2)

        every { customerRepository.findByNameContaining(name, pageable) } returns fakeCustomers

        val customers = customerService.getAll(pageable, name)

        assertEquals(fakeCustomers, customers)
        verify(exactly = 0) { customerRepository.findAll(pageable) }
        verify(exactly = 1) { customerRepository.findByNameContaining(name, pageable) }
    }

    @Test
    fun `should create customer and encrypt password`() {
        val initialPassword = Random.nextInt().toString()
        val fakeCustomer = buildCustomer(password = initialPassword)
        val fakePassword = UUID.randomUUID().toString()
        val fakeCustomerEncrypted = fakeCustomer.copy(password = fakePassword)

        every { customerRepository.save(fakeCustomerEncrypted) } returns fakeCustomer
        every { bCrypt.encode(initialPassword) } returns fakePassword

        customerService.create(fakeCustomer)

        verify(exactly = 1) { customerRepository.save(fakeCustomerEncrypted) }
        verify(exactly = 1) { bCrypt.encode(initialPassword) }
    }

    @Test
    fun `should return customer by id`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

        val customer = customerService.findById(id)

        assertEquals(fakeCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should throw not found when find by id`() {
        val id = Random.nextInt()

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException> { customerService.findById(id) }

        assertEquals("Customer id ${id} not exists!", error.message)
        assertEquals("BS-1101", error.errorCode)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should update customer`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        customerService.update(fakeCustomer)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `should throw not found exception when update customer`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns false
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        val error = assertThrows<NotFoundException> { customerService.update(fakeCustomer) }

        assertEquals("Customer id ${id} not exists!", error.message)
        assertEquals("BS-1101", error.errorCode)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should delete customer`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INATIVO)

        every { customerService.findById(id) } returns fakeCustomer
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer
        every { bookService.deleteByCustomer(fakeCustomer) } just runs

        customerService.delete(id)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }
    }

    @Test
    fun `should throw not found exception when delete customer`() {
        val id = Random.nextInt()

        every { customerService.findById(id) } throws NotFoundException(Errors.BS1101.message.format(id), Errors.BS1101.code)

        val error = assertThrows<NotFoundException> { customerService.delete(id) }

        assertEquals("Customer id ${id} not exists!", error.message)
        assertEquals("BS-1101", error.errorCode)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should return true when email available`() {
        val email = buildCustomer().email

        every { customerRepository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }
    @Test
    fun `should return true when email unavailable`() {
        val email = buildCustomer().email

        every { customerRepository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }
}