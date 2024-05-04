package com.example.tickets.ui.offers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Html.fromHtml
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tickets.App
import com.example.tickets.databinding.FragmentOffersBinding
import com.example.tickets.databinding.HintsBinding
import com.example.tickets.databinding.PopularDirectionsBinding
import com.example.tickets.model.AnyPO
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.reflect.KProperty0


private val dayAndMonthFormat = SimpleDateFormat("dd MMM", Locale("ru"))

private val dayOfWeekFormat = SimpleDateFormat("EE", Locale("ru"))

class OffersFragment : Fragment() {

    private var _binding: FragmentOffersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var adapter: ListDelegationAdapter<List<AnyPO>>

    @Inject
    lateinit var viewModel: OffersViewModel

    private var preferences: SharedPreferences? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentOffersBinding.inflate(inflater, container, false).run {

        super.onCreateView(inflater, container, savedInstanceState)
        preferences = activity?.getPreferences(Context.MODE_PRIVATE)?.also {
            fun KProperty0<EditText>.readFromCache() = get().setText(it.getString(name, ""))
            search::departure.readFromCache()
            search::arrival.readFromCache()
        }
        (requireActivity().applicationContext as App).component.inject(this@OffersFragment)
        _binding = this

        ticketDetails.date.text = format(Date())
        itemsList.root.let {
            it.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            it.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            it.adapter = adapter
        }
        search.arrival.setOnFocusChangeListener { _, hasFocus ->
            onArrivalFocusChanged(hasFocus)
        }

        fun SharedPreferences.Editor.writeProperty(property: KProperty0<EditText>) =
            putString(property.name, "${property().text}")

        search.arrival.addTextChangedListener { _ ->
            preferences?.edit()?.writeProperty(search::arrival)?.apply()
            if (search.arrival.text.isNotBlank()) goToScreen3() else goToScreen2()
        }
        search.departure.addTextChangedListener { _ ->
            preferences?.edit()?.writeProperty(search::departure)?.apply()
        }
        search.directionsExchange.setOnClickListener {
            val tmp = search.arrival.text
            search.arrival.text = search.departure.text
            search.departure.text = tmp
        }
        ticketDetails.date.setOnClickListener(ticketDetails.date.datePickerListener())
        ticketDetails.backDate.setOnClickListener(ticketDetails.backDate.datePickerListener {
            ticketDetails.plus.visibility = GONE
        })
        watchAllTickets.setOnClickListener {
            startActivity(Intent(activity, OffersFragment::class.java))
        }
        popularDirections.init()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect {
                    adapter.items = it
                    adapter.notifyDataSetChanged()
                }
            }
        }
        hints.init()
        if (search.arrival.text.isNotBlank()) goToScreen3()
        root
    }

    private fun TextView.datePickerListener(onPick: () -> Unit = {}): (v: View) -> Unit {
        val calendar = Calendar.getInstance()
        val listener: (v: View) -> Unit = {
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    calendar[Calendar.YEAR] = year
                    calendar[Calendar.MONTH] = month
                    calendar[Calendar.DAY_OF_MONTH] = day
                    text = format(calendar.time)
                    onPick()
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
        return listener
    }

    private fun format(date: Date): Spanned = fromHtml(
        "<font color=#FFFFFF>${
            dayAndMonthFormat.format(date)
        }</font><font color=#9F9F9F>, ${
            dayOfWeekFormat.format(date)
        }</font>",
        FROM_HTML_MODE_LEGACY
    )

    private fun goToScreen1() {
        binding.run {
            hints.root.visibility = GONE
            listTitle.visibility = VISIBLE
            leftArrow.visibility = GONE
            itemsList.root.visibility = VISIBLE
            search.directionsExchange.visibility = GONE
            ticketDetails.root.visibility = GONE
            popularDirections.root.visibility = GONE
            screenTitle.visibility = VISIBLE
            watchAllTickets.visibility = GONE
            listTitle.text = "Музыкально отлететь"
        }
    }

    private fun goToScreen2() {
        binding.run {
            hints.root.visibility = VISIBLE
            listTitle.visibility = GONE
            leftArrow.visibility = GONE
            itemsList.root.visibility = GONE
            search.directionsExchange.visibility = GONE
            ticketDetails.root.visibility = GONE
            popularDirections.root.visibility = VISIBLE
            screenTitle.visibility = GONE
            watchAllTickets.visibility = GONE
            itemsList.root.layoutManager =
                LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            viewModel.itemType = ItemType.OFFER
        }
    }

    private fun goToScreen3() {
        binding.run {
            hints.root.visibility = GONE
            listTitle.visibility = VISIBLE
            leftArrow.visibility = VISIBLE
            itemsList.root.visibility = VISIBLE
            search.directionsExchange.visibility = VISIBLE
            ticketDetails.root.visibility = VISIBLE
            popularDirections.root.visibility = GONE
            screenTitle.visibility = GONE
            watchAllTickets.visibility = VISIBLE
            listTitle.text = "Прямые рельсы"
            itemsList.root.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            viewModel.itemType = ItemType.TICKET_OFFER
        }
    }

    private fun HintsBinding.init() {
        val stubListener: (v: View) -> Unit = {
            AlertDialog.Builder(context).setTitle("Заглушка").setPositiveButton("OK") { _, _ ->
            }.create().show()
        }
        path.setOnClickListener(stubListener)
        anywhere.setOnClickListener { binding.search.arrival.setText("Куда угодно") }
        weekends.setOnClickListener(stubListener)
        hotTickets.setOnClickListener(stubListener)
    }

    private fun onArrivalFocusChanged(hasFocus: Boolean) {
        when {
            hasFocus && binding.search.arrival.text.isBlank() -> goToScreen2()
            binding.search.arrival.text.isBlank() -> goToScreen1()
            else -> goToScreen3()
        }
    }

    private fun PopularDirectionsBinding.init() {
        arrayOf(
            stambulLayout to stambulText, sochiLayout to sochiText, phuketLayout to phuketText
        ).forEach { (layout, text) ->
            layout.setOnClickListener {
                binding.search.arrival.setText(text.text)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}