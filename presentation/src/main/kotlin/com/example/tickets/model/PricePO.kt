package com.example.tickets.model

import com.example.domain.model.Price

data class PricePO(val value: Int)

fun Price.toPO() = PricePO(value)
