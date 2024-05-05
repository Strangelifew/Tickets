package com.example.tickets.ui.offers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Offer
import com.example.domain.model.TicketOffer
import com.example.domain.usecase.OfferUseCases
import com.example.domain.usecase.TicketOfferUseCases
import com.example.tickets.model.AnyPO
import com.example.tickets.model.toPO
import com.example.tickets.ui.offers.ItemType.OFFER
import com.example.tickets.ui.offers.ItemType.TICKET_OFFER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OffersViewModel @Inject constructor(
    private val offerUseCases: OfferUseCases,
    private val ticketOfferUseCases: TicketOfferUseCases
) : ViewModel() {

    private val dataCache: MutableMap<ItemType, List<AnyPO>> = ConcurrentHashMap()

    private val _items = MutableStateFlow<List<AnyPO>>(emptyList())

    val items: Flow<List<AnyPO>> by ::_items

    var calendar: Calendar = Calendar.getInstance()

    var backCalendar: Calendar? = null

    init {
        viewModelScope.launch {
            merge(
                offerUseCases.offers.map { OFFER to it.map(Offer::toPO) },
                ticketOfferUseCases.ticketsOffers.map { TICKET_OFFER to it.map(TicketOffer::toPO) }
            )
                .onEach { (type, items) -> dataCache[type] = items }
                .filter { it.first == itemType }
                .collect { (_, items) -> _items.value = items }
        }
    }

    var itemType: ItemType = OFFER
        set(value) {
            field = value
            _items.value = dataCache[value].orEmpty()
        }
}

enum class ItemType { OFFER, TICKET_OFFER }

