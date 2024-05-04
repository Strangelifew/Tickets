package com.example.data.repository

import com.example.domain.TicketOfferRepository
import com.example.domain.model.TicketOffer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class TicketOfferRepositoryImpl @Inject constructor() : TicketOfferRepository {
    override val ticketsOffers: Flow<List<TicketOffer>> = flow {
        while (true) {
            delay(5.seconds)
            emit(
                Json.decodeFromString(
                    this::class.java.classLoader.getResourceAsStream("offers_tickets.json")!!
                        .reader().readText()
                )
            )
        }
    }
}


