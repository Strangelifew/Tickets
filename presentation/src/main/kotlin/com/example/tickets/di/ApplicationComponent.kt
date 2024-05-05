package com.example.tickets.di

import com.example.tickets.ui.flight.FlightFragment
import com.example.tickets.ui.tickets.TicketsFragment
import com.example.tickets.ui.offers.OffersFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, AdapterModule::class])
interface ApplicationComponent {
    fun inject(fragment: OffersFragment)
    fun inject(fragment: TicketsFragment)
    fun inject(fragment: FlightFragment)
}