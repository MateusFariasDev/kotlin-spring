package com.bookstore.events

import com.bookstore.model.PurchaseModel
import org.springframework.context.ApplicationEvent

class PurchaseEvent (
    source: Any,
    val purchaseModel: PurchaseModel
) : ApplicationEvent(source)