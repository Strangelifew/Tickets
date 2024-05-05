package com.example.data.repository

import com.example.domain.TicketRepository
import com.example.domain.model.Ticket
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalSerializationApi::class)
val json = Json { explicitNulls = false }

class TicketRepositoryImpl @Inject constructor() : TicketRepository {
    override val tickets: Flow<List<Ticket>> = flow {
        while (true) {
            delay(5.seconds)
            emit(
                json.decodeFromString(
                    this::class.java.classLoader.getResourceAsStream("tickets.json")!!.reader()
                        .readText()
                )
            )
        }
    }
}
