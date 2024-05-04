package com.example.domain

import com.example.domain.model.Ticket

interface TicketRepository {
    fun getTickets(): List<Ticket>
}