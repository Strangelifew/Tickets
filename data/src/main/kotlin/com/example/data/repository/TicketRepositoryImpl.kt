package com.example.data.repository

import com.example.domain.TicketRepository
import com.example.domain.model.Ticket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

class TicketRepositoryImpl @Inject constructor() : TicketRepository {
    override fun getTickets(): List<Ticket> = Json.decodeFromString(
        this::class.java.classLoader.getResourceAsStream("tickets.json")!!.reader().readText()
    )
}
