package com.example.domain

import com.example.domain.model.TicketOffer
import kotlinx.coroutines.flow.Flow

interface TicketOfferRepository {
    val ticketsOffers: Flow<List<TicketOffer>>
}