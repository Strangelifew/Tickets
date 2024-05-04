package com.example.data.repository

import com.example.domain.OfferRepository
import com.example.domain.model.Offer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class OfferRepositoryImpl @Inject constructor() : OfferRepository {
    override val offers: Flow<List<Offer>> = flow {
        while (true) {
            delay(5.seconds)
            emit(
                Json.decodeFromString(
                    this::class.java.classLoader.getResourceAsStream("offers.json")!!.reader()
                        .readText()
                )
            )
        }
    }
}
