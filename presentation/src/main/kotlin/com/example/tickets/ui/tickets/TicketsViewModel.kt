package com.example.tickets.ui.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Ticket
import com.example.domain.usecase.TicketUseCases
import com.example.tickets.model.TicketPO
import com.example.tickets.model.toPO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketsViewModel @Inject constructor(private val ticketUseCases: TicketUseCases) :
    ViewModel() {

    private val _items = MutableStateFlow<List<TicketPO>>(emptyList())

    val items: Flow<List<TicketPO>> by ::_items

    init {
        viewModelScope.launch {
            ticketUseCases.tickets.map { it.map(Ticket::toPO) }.collect { _items.value = it }
        }
    }
}

