@file:OptIn(ExperimentalSerializationApi::class)

package com.example.domain.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Ticket (
    val id: Int,
    val badge: String?,
    val price: Price,
    @JsonNames("provider_name")
    val providerName: String,
    val company: String,
    val departure: Departure,
    val arrival: Arrival,
    @JsonNames("has_transfer")
    val hasTransfer: Boolean,
    @JsonNames("has_visa_transfer")
    val hasVisaTransfer: Boolean,
    val luggage: Luggage,
    @JsonNames("hand_luggage")
    val handLuggage: HandLuggage,
    @JsonNames("is_returnable")
    val isReturnable: Boolean,
    @JsonNames("is_exchangeable")
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
    @JsonNames("has_hand_luggage")
    val hasHandLuggage: Boolean,
    val size: String?
)

@Serializable
data class Luggage(
    @JsonNames("has_luggage")
    val hasLuggage: Boolean,
    val price: Price?
)
