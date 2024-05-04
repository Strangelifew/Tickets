package com.example.tickets.di

import com.example.tickets.R
import com.example.tickets.databinding.MusicTravelItemBinding
import com.example.tickets.databinding.TicketOfferItemBinding
import com.example.tickets.model.AnyPO
import com.example.tickets.model.OfferPO
import com.example.tickets.model.TicketOfferPO
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import dagger.Module
import dagger.Provides

@Module
class AdapterModule {
    @Provides
    fun offersAdapter(): ListDelegationAdapter<List<AnyPO>> =
        ListDelegationAdapter(
            adapterDelegateViewBinding<OfferPO, _, MusicTravelItemBinding>(
                { inflater, parent ->
                    MusicTravelItemBinding.inflate(inflater, parent, false)
                }
            ) { bind { bindOffer() } },
            adapterDelegateViewBinding<TicketOfferPO, _, TicketOfferItemBinding>(
                { inflater, parent ->
                    TicketOfferItemBinding.inflate(inflater, parent, false)
                }
            ) { bind { bindTicketOffer() } }
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
    binding.price.text = "от ${
        String.format("%,d", item.price.value).replace(',', ' ')
    } ₽"
    binding.title.text = item.title
}

private fun AdapterDelegateViewBindingViewHolder<TicketOfferPO, TicketOfferItemBinding>.bindTicketOffer() {
    binding.run {
        title.text = item.title
        timeRange.text = item.timeRange.joinToString(" ")

        price.text = "${String.format("%,d", item.price.value).replace(',', ' ')} ₽"
        roundIcon.setImageResource(
            when (absoluteAdapterPosition) {
                0 -> R.drawable.offers_tickets_1
                1 -> R.drawable.offers_tickets_2
                else -> R.drawable.offers_tickets_3
            }
        )
    }
}