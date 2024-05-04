package com.example.tickets.ui.offers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Offer
import com.example.domain.model.TicketOffer
import com.example.domain.usecase.OfferUseCases
import com.example.domain.usecase.TicketOfferUseCases
import com.example.tickets.model.OfferPO
import com.example.tickets.model.TicketOfferPO
import com.example.tickets.ui.offers.ItemType.*
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

class OffersViewModel @Inject constructor(
    private val offerUseCases: OfferUseCases,
    private val ticketOfferUseCases: TicketOfferUseCases
) : ViewModel() {
    private val offers: StateFlow<List<OfferPO>> =
        MutableStateFlow<List<OfferPO>>(emptyList()).apply {
            viewModelScope.launch {
                offerUseCases.offers.collect { value = it.map(Offer::toPO) }
            }
        }

    private val ticketsOffers: StateFlow<List<TicketOfferPO>> =
        MutableStateFlow<List<TicketOfferPO>>(emptyList()).apply {
            viewModelScope.launch {
                ticketOfferUseCases.ticketsOffers.collect { value = it.map(TicketOffer::toPO) }
            }
        }

    private val _items = MutableStateFlow<List<Any>>(emptyList())

    val items: Flow<List<Any>> by ::_items

    init {
        viewModelScope.launch {
            merge(
                offerUseCases.offers.map { OFFER to it.map(Offer::toPO) },
                ticketOfferUseCases.ticketsOffers.map { TICKET_OFFER to it.map(TicketOffer::toPO) }
            ).filter { it.first == itemType }.collect { (_, items) ->
                _items.value = items
            }
        }
    }

    var itemType: ItemType = OFFER
        set(value) {
            field = value
            _items.value = when (value) {
                OFFER -> offers.value
                TICKET_OFFER -> ticketsOffers.value
            }
        }
}

enum class ItemType { OFFER, TICKET_OFFER }

private fun Offer.toPO() = OfferPO(id, title, town, price)

private fun TicketOffer.toPO() = TicketOfferPO(id, title, timeRange, price)
