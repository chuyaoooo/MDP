package com.example.campuspass.ui.student.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuspass.data.model.Event
import com.example.campuspass.data.model.EventCategory
import com.example.campuspass.data.repository.EventRepository
import com.example.campuspass.data.repository.SampleEventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/** The filter chips shown under the search box, in Figma order. */
enum class MarketplaceFilter(val label: String) {
    ALL("All"),
    CAREERS("Careers"),
    SKILLS("Skills"),
    THIS_WEEK("This week")
}

/** One event card, already formatted for display. */
data class EventCardUi(
    val id: String,
    val title: String,
    val dateLine: String,
    val pointsLine: String
)

data class MarketplaceUiState(
    val query: String = "",
    val selectedFilter: MarketplaceFilter = MarketplaceFilter.ALL,
    val events: List<EventCardUi> = emptyList(),
    val isLoading: Boolean = true
) {
    val resultsLabel: String
        get() = "Upcoming events · ${events.size} ${if (events.size == 1) "result" else "results"}"
}

/**
 * Search and filter logic for the Discover events screen.
 * The screen only reads [uiState] and reports user actions back here.
 */
class MarketplaceViewModel(
    repository: EventRepository = SampleEventRepository,
    private val now: () -> LocalDateTime = { LocalDateTime.now() }
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val filter = MutableStateFlow(MarketplaceFilter.ALL)

    val uiState: StateFlow<MarketplaceUiState> =
        combine(repository.approvedEvents(), query, filter) { events, q, f ->
            MarketplaceUiState(
                query = q,
                selectedFilter = f,
                events = filterEvents(events, q, f, now()).map(::toCardUi),
                isLoading = false
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MarketplaceUiState())

    fun onQueryChange(value: String) { query.value = value }

    fun onFilterSelected(value: MarketplaceFilter) { filter.value = value }

    companion object {
        private val dateFormat = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH)
        private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

        /** Keeps upcoming events that match the search text and chip, soonest first. */
        fun filterEvents(
            events: List<Event>,
            query: String,
            filter: MarketplaceFilter,
            now: LocalDateTime
        ): List<Event> {
            val text = query.trim()
            return events
                .filter { it.end.isAfter(now) }
                .filter { text.isEmpty() || it.title.contains(text, ignoreCase = true) || it.organiser.contains(text, ignoreCase = true) }
                .filter {
                    when (filter) {
                        MarketplaceFilter.ALL -> true
                        MarketplaceFilter.CAREERS -> it.category == EventCategory.CAREERS
                        MarketplaceFilter.SKILLS -> it.category == EventCategory.SKILLS
                        MarketplaceFilter.THIS_WEEK -> it.start.isBefore(now.plusDays(7))
                    }
                }
                .sortedBy { it.start }
        }

        /** Formats an event the way the Figma card shows it. */
        fun toCardUi(event: Event) = EventCardUi(
            id = event.id,
            title = event.title,
            dateLine = "${event.start.format(dateFormat)} · ${event.start.format(timeFormat)}–${event.end.format(timeFormat)}",
            pointsLine = if (event.isFull) "${event.cpdPoints} CPD points · Event full"
            else "${event.cpdPoints} CPD points · ${event.placesLeft} ${if (event.placesLeft == 1) "place" else "places"} left"
        )
    }
}
