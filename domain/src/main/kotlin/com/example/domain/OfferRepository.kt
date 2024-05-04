package com.example.domain

import com.example.domain.model.Offer
import kotlinx.coroutines.flow.Flow

interface OfferRepository {
    val offers: Flow<List<Offer>>
}