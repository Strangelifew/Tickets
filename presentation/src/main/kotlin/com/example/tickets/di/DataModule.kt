package com.example.tickets.di

import com.example.data.repository.OfferRepositoryImpl
import com.example.data.repository.TicketOfferRepositoryImpl
import com.example.data.repository.TicketRepositoryImpl
import com.example.domain.OfferRepository
import com.example.domain.TicketOfferRepository
import com.example.domain.TicketRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {
    @Binds
    fun bindOfferRepository(repo: OfferRepositoryImpl): OfferRepository

    @Binds
    fun bindTicketOfferRepository(repo: TicketOfferRepositoryImpl): TicketOfferRepository

    @Binds
    fun bindTicketRepository(repo: TicketRepositoryImpl): TicketRepository
}