package com.example.domain.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class TicketOffer(
    val id: Int,
    val title: String,
    @JsonNames("time_range")
    val timeRange: List<String>,
    val price: Price
)