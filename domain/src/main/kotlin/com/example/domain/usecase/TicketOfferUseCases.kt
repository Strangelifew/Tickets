package com.example.domain.usecase

import com.example.domain.TicketOfferRepository
import javax.inject.Inject


class TicketOfferUseCases @Inject constructor(repository: TicketOfferRepository) {
    val ticketsOffers by repository::ticketsOffers
}