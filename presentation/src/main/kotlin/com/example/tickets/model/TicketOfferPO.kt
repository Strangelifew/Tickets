package com.example.tickets.model

import com.example.domain.model.TicketOffer

data class TicketOfferPO(
    val id: Int,
    val title: String,
    val timeRange: List<String>,
    val price: PricePO
) : AnyPO

fun TicketOffer.toPO() = TicketOfferPO(id, title, timeRange, price.toPO())