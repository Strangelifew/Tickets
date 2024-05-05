package com.example.tickets.ui.flight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.tickets.App
import com.example.tickets.R
import com.example.tickets.databinding.FragmentFlightBinding
import com.example.tickets.ui.offers.OffersFragment
import com.example.tickets.ui.tickets.TicketsFragment
import javax.inject.Inject

class FlightFragment: Fragment() {

    @Inject
    lateinit var viewModel: CommonViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        (requireActivity().applicationContext as App).component.inject(this)
        return FragmentFlightBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.commit {
            val fragment = when(viewModel.state) {
                FlightState.OFFERS -> OffersFragment()
                FlightState.TICKETS -> TicketsFragment()
            }
            replace(R.id.fragment_container, fragment)
        }
    }
}