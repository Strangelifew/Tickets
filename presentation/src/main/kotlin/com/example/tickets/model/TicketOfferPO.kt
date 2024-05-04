package com.example.tickets.model

import com.example.domain.model.Price

data class TicketOfferPO(
    val id: Int,
    val title: String,
    val timeRange: List<String>,
    val price: Price
)
