package com.example.data.repository

import com.example.domain.TicketOfferRepository
import com.example.domain.model.TicketOffer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class TicketOfferRepositoryImpl @Inject constructor() : TicketOfferRepository {
    override val ticketsOffers: Flow<List<TicketOffer>> = flow {
        while (true) {
            delay(5.seconds)
            emit(
                json.decodeFromString(
                    this::class.java.classLoader.getResourceAsStream("offers_tickets.json")!!
                        .reader().readText()
                )
            )
        }
    }
}


