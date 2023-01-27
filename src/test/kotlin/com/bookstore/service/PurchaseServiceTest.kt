package com.bookstore.service

import com.bookstore.enums.CustomerStatus
import com.bookstore.enums.Role
import com.bookstore.events.PurchaseEvent
import com.bookstore.helper.buildCustomer
import com.bookstore.helper.buildPurchase
import com.bookstore.model.BookModel
import com.bookstore.model.CustomerModel
import com.bookstore.model.PurchaseModel
import com.bookstore.repository.PurchaseRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import java.math.BigDecimal
import java.util.*

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {

    @MockK
    private lateinit var purchaseRepository: PurchaseRepository

    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService

    val purchaseEventSlot = slot<PurchaseEvent>()

    @Test
    fun `should create and publish event`() {
        val fakePurchase = buildPurchase()

        every { purchaseRepository.save(fakePurchase) } returns fakePurchase
        every { applicationEventPublisher.publishEvent(any()) } just runs

        purchaseService.create(fakePurchase)

        verify(exactly = 1) { purchaseRepository.save(fakePurchase) }
        verify(exactly = 1) { applicationEventPublisher.publishEvent(capture(purchaseEventSlot)) }

        assertEquals(fakePurchase, purchaseEventSlot.captured.purchaseModel)
    }

    @Test
    fun `should update purchase`() {
        val fakePurchase = buildPurchase()

        every { purchaseRepository.save(fakePurchase) } returns fakePurchase

        purchaseService.update(fakePurchase)

        verify(exactly = 1) { purchaseRepository.save(fakePurchase) }
    }
}