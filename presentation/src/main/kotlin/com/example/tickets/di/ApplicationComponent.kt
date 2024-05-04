package com.example.tickets.di

import com.example.tickets.ui.offers.OffersFragment
import dagger.Component

@Component(modules = [DataModule::class, AdapterModule::class])
interface ApplicationComponent {
    fun inject(fragment: OffersFragment)
}