package com.bookstore.events.listener

import com.bookstore.events.PurchaseEvent
import com.bookstore.service.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GenerateNfeListener(
    private val purchaseService: PurchaseService
) {
    @Async
    @EventListener
    fun listen(purchaseEvent: PurchaseEvent) {
        val nfe = UUID.randomUUID().toString()
        val purchase = purchaseEvent.purchaseModel.copy(nfe = nfe)
        purchaseService.update(purchase)
    }
}