package com.example.tickets.di

import android.view.View.*
import com.example.tickets.R
import com.example.tickets.databinding.MusicTravelItemBinding
import com.example.tickets.databinding.TicketItemBinding
import com.example.tickets.databinding.TicketOfferItemBinding
import com.example.tickets.model.AnyPO
import com.example.tickets.model.OfferPO
import com.example.tickets.model.PricePO
import com.example.tickets.model.TicketOfferPO
import com.example.tickets.model.TicketPO
import com.example.tickets.ui.offers.locale
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat

@Module
class AdapterModule {
    @Provides
    fun offersAdapter(): ListDelegationAdapter<List<AnyPO>> =
        ListDelegationAdapter(
            adapterDelegateViewBinding<OfferPO, _, _>(
                { inflater, parent ->
                    MusicTravelItemBinding.inflate(inflater, parent, false)
                }
            ) { bind { bindOffer() } },
            adapterDelegateViewBinding<TicketOfferPO, _, _>(
                { inflater, parent ->
                    TicketOfferItemBinding.inflate(inflater, parent, false)
                }
            ) { bind { bindTicketOffer() } }
        )

    @Provides
    fun ticketsAdapter(): ListDelegationAdapter<List<TicketPO>> =
        ListDelegationAdapter(
            adapterDelegateViewBinding(
                { inflater, parent ->
                    TicketItemBinding.inflate(inflater, parent, false)
                }
            ) {
                bind {
                    binding.run {
                        if (item.badge != null) binding.badge.text = item.badge
                        else binding.badge.visibility = GONE
                        price.text = item.price.format()
                        val departureDate = isoDateTimeFormat.parse(item.departure.date)!!
                        val arrivalDate = isoDateTimeFormat.parse(item.arrival.date)!!
                        departureTime.text = hoursAndMinutesFormat.format(departureDate)
                        arrivalTime.text = hoursAndMinutesFormat.format(arrivalDate)
                        departureAirport.text = item.departure.airport
                        arrivalAirport.text = item.arrival.airport
                        val formattedDuration = String.format(
                            "%.1f",
                            (arrivalDate.time - departureDate.time) / 1000.0 / 3600
                        )
                        pathTimeAndPathType.text = "${formattedDuration}ч в пути${
                            "/Без пересадок".takeUnless { item.hasTransfer }.orEmpty()
                        }"
                    }
                }
            }
        )
}

private fun AdapterDelegateViewBindingViewHolder<OfferPO, MusicTravelItemBinding>.bindOffer() {
    binding.image.setImageResource(
        when (item.id) {
            1 -> R.drawable.musical_travel_1
            2 -> R.drawable.musical_travel_2
            else -> R.drawable.musical_travel_3
        }
    )
    binding.town.text = item.town
    binding.price.text = "от ${item.price.format()}"
    binding.title.text = item.title
}

private fun PricePO.format() = "${String.format("%,d", value).replace(',', ' ')} ₽"

private fun AdapterDelegateViewBindingViewHolder<TicketOfferPO, TicketOfferItemBinding>.bindTicketOffer() {
    binding.run {
        title.text = item.title
        timeRange.text = item.timeRange.joinToString(" ")

        price.text = item.price.format()
        roundIcon.setImageResource(
            when (absoluteAdapterPosition) {
                0 -> R.drawable.offers_tickets_1
                1 -> R.drawable.offers_tickets_2
                else -> R.drawable.offers_tickets_3
            }
        )
    }
}

private val isoDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale)
private val hoursAndMinutesFormat = SimpleDateFormat("HH:mm", locale)
