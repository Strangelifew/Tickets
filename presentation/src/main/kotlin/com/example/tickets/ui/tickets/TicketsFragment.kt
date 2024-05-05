package com.example.tickets.ui.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tickets.App
import com.example.tickets.R
import com.example.tickets.databinding.FragmentTicketsBinding
import com.example.tickets.model.TicketPO
import com.example.tickets.ui.flight.CommonViewModel
import com.example.tickets.ui.offers.OffersFragment
import com.example.tickets.ui.offers.locale
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

private val dayAndMonthFormat = SimpleDateFormat("dd MMMM", locale)

class TicketsFragment : Fragment() {

    private var _binding: FragmentTicketsBinding? = null

    @Inject
    lateinit var adapter: ListDelegationAdapter<List<TicketPO>>

    @Inject
    lateinit var viewModel: TicketsViewModel

    @Inject
    lateinit var commonViewModel: CommonViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTicketsBinding.inflate(inflater, container, false).run {
        super.onCreateView(inflater, container, savedInstanceState)
        (requireActivity().applicationContext as App).component.inject(this@TicketsFragment)
        _binding = this
        itemsList.root.let {
            it.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            it.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            it.adapter = adapter
        }
        subscribeToItems()
        val calendar = commonViewModel.calendar
        val departure = commonViewModel.departure
        val arrival = commonViewModel.arrival
        departureArrival.text = "$departure-$arrival"
        datePassengers.text = "${dayAndMonthFormat.format(calendar.time)}, 1 пассажир"
        back.setOnClickListener {
            commonViewModel.toOffers()
            parentFragmentManager.commit { replace(R.id.fragment_container, OffersFragment()) }
        }
        root
    }

    private fun subscribeToItems() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect {
                    adapter.items = it
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}