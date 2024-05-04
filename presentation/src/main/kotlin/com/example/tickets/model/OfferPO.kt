package com.example.tickets.model

import com.example.domain.model.Price

data class OfferPO(
    val id: Int,
    val title : String,
    val town: String,
    val price: Price,
)