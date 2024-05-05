package com.example.domain.usecase

import com.example.domain.OfferRepository
import com.example.domain.model.Offer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfferUseCases @Inject constructor(repository: OfferRepository) {
    val offers: Flow<List<Offer>> by repository::offers
}