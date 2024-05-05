package com.example.tickets.model

import com.example.domain.model.Offer

data class OfferPO(
    val id: Int,
    val title: String,
    val town: String,
    val price: PricePO,
) : AnyPO

fun Offer.toPO() = OfferPO(id, title, town, price.toPO())