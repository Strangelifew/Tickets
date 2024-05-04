package com.example.tickets

import android.app.Application
import com.example.tickets.di.ApplicationComponent
import com.example.tickets.di.DaggerApplicationComponent

class App: Application() {
    val component: ApplicationComponent = DaggerApplicationComponent.create()
}