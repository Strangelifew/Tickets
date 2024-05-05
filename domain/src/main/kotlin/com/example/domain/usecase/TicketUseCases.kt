package com.example.domain.usecase

import com.example.domain.TicketRepository
import javax.inject.Inject

class TicketUseCases @Inject constructor(repository: TicketRepository) {
    val tickets by repository::tickets
}