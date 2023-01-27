package com.bookstore.model

import com.bookstore.enums.BookStatus
import com.bookstore.enums.Errors
import com.bookstore.exception.BadRequestException
import jakarta.persistence.*
import java.math.BigDecimal

@Entity(name = "book")
data class BookModel (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerModel? = null
) {

    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
        set (value) {
            if (field == BookStatus.CANCELADO || field == BookStatus.DELETADO) {
                throw BadRequestException(Errors.BS1002.message.format(field), Errors.BS1002.code)
            }

            field = value
        }

    constructor(
        id: Int? = null,
        name: String,
        price: BigDecimal,
        status: BookStatus?,
        customer: CustomerModel? = null
    ): this(
        id,
        name,
        price,
        customer
    ) {
        this.status = status
    }
}