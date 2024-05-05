package com.example.tickets.ui.offers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
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
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tickets.App
import com.example.tickets.R
import com.example.tickets.databinding.FragmentOffersBinding
import com.example.tickets.databinding.HintsBinding
import com.example.tickets.databinding.PopularDirectionsBinding
import com.example.tickets.model.AnyPO
import com.example.tickets.ui.flight.CommonViewModel
import com.example.tickets.ui.offers.OffersFragment.Screen.SCREEN_1
import com.example.tickets.ui.offers.OffersFragment.Screen.SCREEN_2
import com.example.tickets.ui.offers.OffersFragment.Screen.SCREEN_3
import com.example.tickets.ui.tickets.TicketsFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.reflect.KProperty0


val locale = Locale("ru")
private val dayAndMonthFormat = SimpleDateFormat("dd MMM", locale).apply {
    dateFormatSymbols = DateFormatSymbols.getInstance(locale).apply {
        shortMonths = arrayOf(
            "янв", "фев", "мар", "апр", "май", "июн",
            "июл", "авг", "сен", "окт", "ноя", "дек"
        )
    }
}

private val dayOfWeekFormat = SimpleDateFormat("EE", locale)

class OffersFragment : Fragment() {

    private var _binding: FragmentOffersBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var adapter: ListDelegationAdapter<List<AnyPO>>

    @Inject
    lateinit var viewModel: OffersViewModel

    @Inject
    lateinit var commonViewModel: CommonViewModel

    private var preferences: SharedPreferences? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentOffersBinding.inflate(inflater, container, false).run {

        super.onCreateView(inflater, container, savedInstanceState)
        (requireActivity().applicationContext as App).component.inject(this@OffersFragment)
        _binding = this
        preferences = initPreferences()

        ticketDetails.date.text = format(viewModel.calendar.time)
        viewModel.backCalendar?.time?.let {
            ticketDetails.plus.visibility = GONE
            ticketDetails.backDate.text = format(it)
        }
        initRecycleView()
        search.arrival.setOnFocusChangeListener { _, hasFocus -> onArrivalFocusChanged(hasFocus) }

        search.arrival.addTextChangedListener { onArrivalTextChanged() }
        search.departure.addTextChangedListener {
            preferences?.edit()?.writeProperty(search::departure)?.apply()
        }
        search.directionsExchange.setOnClickListener {
            val tmp = search.arrival.text
            search.arrival.text = search.departure.text
            search.departure.text = tmp
        }
        ticketDetails.date.setOnClickListener(
            ticketDetails.date.datePickerListener { viewModel.calendar = it }
        )
        ticketDetails.backDate.setOnClickListener(
            ticketDetails.backDate.datePickerListener {
                viewModel.backCalendar = it
                ticketDetails.plus.visibility = GONE
            }
        )
        watchAllTickets.setOnClickListener { goToTickets(viewModel.calendar) }
        popularDirections.init()
        subscribeToItems()
        hints.init()
        if (search.arrival.text.isNotBlank()) SCREEN_3(binding, requireActivity(), viewModel)
        root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun FragmentOffersBinding.goToTickets(calendar: Calendar) {
        commonViewModel.toTickets(
            calendar = calendar,
            departure = "${search.departure.text}",
            arrival = "${search.arrival.text}"
        )
        parentFragmentManager.commit { replace(R.id.fragment_container, TicketsFragment()) }
    }

    private fun FragmentOffersBinding.onArrivalTextChanged() {
        preferences?.edit()?.writeProperty(search::arrival)?.apply()
        if (search.arrival.text.isBlank()) SCREEN_2(binding, requireActivity(), viewModel)
        else SCREEN_3(binding, requireActivity(), viewModel)
    }

    private fun FragmentOffersBinding.initRecycleView() {
        itemsList.root.let {
            it.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            it.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            it.adapter = adapter
        }
    }

    private fun initPreferences(): SharedPreferences? =
        activity?.getPreferences(Context.MODE_PRIVATE)?.also {
            fun KProperty0<EditText>.readFromCache() = get().setText(it.getString(name, ""))
            binding.search::departure.readFromCache()
            binding.search::arrival.readFromCache()
        }

    private fun TextView.datePickerListener(
        onPick: (Calendar) -> Unit
    ): (v: View) -> Unit = Calendar.getInstance().let { calendar ->
        {
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    calendar[Calendar.YEAR] = year
                    calendar[Calendar.MONTH] = month
                    calendar[Calendar.DAY_OF_MONTH] = day
                    text = format(calendar.time)
                    onPick(calendar)
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
    }

    private fun format(date: Date): Spanned = fromHtml(
        "<font color=#FFFFFF>${
            dayAndMonthFormat.format(date)
        }</font><font color=#9F9F9F>, ${
            dayOfWeekFormat.format(date)
        }</font>",
        FROM_HTML_MODE_LEGACY
    )

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
        val screen = when {
            hasFocus && binding.search.arrival.text.isBlank() -> SCREEN_2
            binding.search.arrival.text.isBlank() -> SCREEN_1
            else -> SCREEN_3
        }
        screen(binding, requireActivity(), viewModel)
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

    private fun SharedPreferences.Editor.writeProperty(property: KProperty0<EditText>) =
        putString(property.name, "${property().text}")

    private enum class Screen(
        private val init: FragmentOffersBinding.(Activity, OffersViewModel) -> Unit = { _, _ -> },
        private vararg val visibleViews: FragmentOffersBinding.() -> View
    ) {
        SCREEN_1(
            { _, _ -> listTitle.text = "Музыкально отлететь" },
            FragmentOffersBinding::listTitle,
            { itemsList.root },
            FragmentOffersBinding::screenTitle
        ),
        SCREEN_2(
            { activity, viewModel ->
                itemsList.root.layoutManager =
                    LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
                viewModel.itemType = ItemType.OFFER
            },
            { hints.root },
            { popularDirections.root },
        ),
        SCREEN_3(
            { activity, viewModel ->
                listTitle.text = "Прямые рельсы"
                itemsList.root.layoutManager =
                    LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                viewModel.itemType = ItemType.TICKET_OFFER
            },
            FragmentOffersBinding::listTitle,
            FragmentOffersBinding::leftArrow,
            { itemsList.root },
            { search.directionsExchange },
            { ticketDetails.root },
            FragmentOffersBinding::watchAllTickets
        );

        operator fun invoke(
            binding: FragmentOffersBinding,
            activity: Activity,
            viewModel: OffersViewModel
        ) {
            val allViews = ALL_VIEWS.mapTo(hashSetOf()) { it(binding) }
            val visibleViews = visibleViews.mapTo(hashSetOf()) { it(binding) }
            allViews.forEach { it.visibility = (if (it in visibleViews) VISIBLE else GONE) }
            init(binding, activity, viewModel)
        }

        private companion object {
            val ALL_VIEWS = Screen.entries.flatMapTo(hashSetOf()) { it.visibleViews.asIterable() }
        }
    }
}