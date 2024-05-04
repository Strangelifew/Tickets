package com.example.domain.usecase

import com.example.domain.TicketOfferRepository
import javax.inject.Inject


class TicketOfferUseCases @Inject constructor(private val repository: TicketOfferRepository) {
    val ticketsOffers = repository.ticketsOffers
}