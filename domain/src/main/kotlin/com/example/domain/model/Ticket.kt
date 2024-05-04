package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val id: Int,
    val badge: String?,
    val price: Price,
    val providerName: String,
    val company: String,
    val departure: Departure,
    val arrival: Arrival,
    val hasTransfer: Boolean,
    val hasVisaTransfer: Boolean,
    val luggage: Luggage,
    val handLuggage: HandLuggage,
    val isReturnable: Boolean,
    val isExchangeable: Boolean
)

@Serializable
data class Arrival(
    val town: String,
    val date: String,
    val airport: String
)

@Serializable
data class Departure(
    val town: String,
    val date: String,
    val airport: String
)

@Serializable
data class HandLuggage(
    val hasHandLuggage: Boolean,
    val size: String
)

@Serializable
data class Luggage(
    val hasLuggage: Boolean,
    val price: Price
)
