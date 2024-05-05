package com.example.domain

import com.example.domain.model.Ticket
import kotlinx.coroutines.flow.Flow

interface TicketRepository {
    val tickets: Flow<List<Ticket>>
}