package com.bookstore.repository

import com.bookstore.helper.buildCustomer
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @Test
    fun `should return name containing`() {
        val customerCarlos = customerRepository.save(buildCustomer(name = "Carlos"))
        val customerCarina = customerRepository.save(buildCustomer(name = "Carina"))
        customerRepository.save(buildCustomer(name = "Pedro"))

        val pageable = PageRequest.of(0, 10)
        val fakeCustomers = PageImpl(listOf(customerCarlos, customerCarina), pageable, 2)

        val customers = customerRepository.findByNameContaining(name = "ca", pageable)

        assertEquals(fakeCustomers.content, customers.content)
    }

    @Nested
    inner class `exists by email` {
        @Test
        fun `should return true when email exists`() {
            val email = "email@test.com"
            customerRepository.save(buildCustomer(email = email))

            val exists = customerRepository.existsByEmail(email)

            assertTrue(exists)
        }

        @Test
        fun `should return false when email do not exists`() {
            val email = "nonexistingemail@test.com"

            val exists = customerRepository.existsByEmail(email)

            assertFalse(exists)
        }
    }

    @Nested
    inner class `find by email` {
        @Test
        fun `should return customer when email exists`() {
            val email = "email@test.com"
            val fakeCustomer = customerRepository.save(buildCustomer(email = email))

            val result = customerRepository.findByEmail(email)

            assertNotNull(result)
            assertEquals(fakeCustomer, result)
        }

        @Test
        fun `should return null when email do not exists`() {
            val email = "nonexistingemail@test.com"

            val result = customerRepository.findByEmail(email)

            assertNull(result)
        }
    }
}