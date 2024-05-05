package com.example.tickets.model

import com.example.domain.model.Arrival
import com.example.domain.model.Departure
import com.example.domain.model.HandLuggage
import com.example.domain.model.Luggage
import com.example.domain.model.Ticket

data class TicketPO(
    val id: Int,
    val badge: String?,
    val price: PricePO,
    val providerName: String,
    val company: String,
    val departure: DeparturePO,
    val arrival: ArrivalPO,
    val hasTransfer: Boolean,
    val hasVisaTransfer: Boolean,
    val luggage: LuggagePO,
    val handLuggage: HandLuggagePO,
    val isReturnable: Boolean,
    val isExchangeable: Boolean
)

data class ArrivalPO(
    val town: String,
    val date: String,
    val airport: String
)

data class DeparturePO(
    val town: String,
    val date: String,
    val airport: String
)

data class HandLuggagePO(
    val hasHandLuggage: Boolean,
    val size: String?
)

data class LuggagePO(
    val hasLuggage: Boolean,
    val price: PricePO?
)

fun Ticket.toPO() = TicketPO(
    id = id,
    badge = badge,
    price = price.toPO(),
    providerName = providerName,
    company = company,
    departure = departure.toPO(),
    arrival = arrival.toPO(),
    hasTransfer = hasTransfer,
    hasVisaTransfer = hasVisaTransfer,
    luggage = luggage.toPO(),
    handLuggage = handLuggage.toPO(),
    isReturnable = isReturnable,
    isExchangeable = isExchangeable
)

fun Departure.toPO() = DeparturePO(
    town = town,
    date = date,
    airport = airport
)

fun Arrival.toPO() = ArrivalPO(
    town = town,
    date = date,
    airport = airport
)

fun Luggage.toPO() = LuggagePO(
    hasLuggage, price?.toPO()
)

fun HandLuggage.toPO() = HandLuggagePO(
    hasHandLuggage, size
)