package com.example.tickets.ui.flight

import androidx.lifecycle.ViewModel
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommonViewModel @Inject constructor() : ViewModel() {
    var state: FlightState = FlightState.OFFERS
        private set
    var calendar: Calendar = Calendar.getInstance()
        private set
    var departure: String = ""
        private set
    var arrival: String = ""
        private set

    fun toTickets(calendar: Calendar, departure: String, arrival: String) {
        state = FlightState.TICKETS
        this.calendar = calendar
        this.departure = departure
        this.arrival = arrival
    }

    fun toOffers() {
        state = FlightState.OFFERS
    }
}

enum class FlightState { OFFERS, TICKETS }