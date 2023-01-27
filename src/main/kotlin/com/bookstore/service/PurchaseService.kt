package com.bookstore.service

import com.bookstore.events.PurchaseEvent
import com.bookstore.model.PurchaseModel
import com.bookstore.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    fun create(purchase: PurchaseModel) {
        purchaseRepository.save(purchase)

        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchase))
    }

    fun update(purchase: PurchaseModel) {
        purchaseRepository.save(purchase)
    }
}
