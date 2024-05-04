package com.example.domain.usecase

import com.example.domain.TicketRepository
import javax.inject.Inject

class TicketUseCases @Inject constructor(private val repository: TicketRepository) {
    fun getTickets() = repository.getTickets()
}