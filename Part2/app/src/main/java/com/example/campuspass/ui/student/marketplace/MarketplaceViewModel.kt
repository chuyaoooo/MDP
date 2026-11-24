package com.example.campuspass.ui.student.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuspass.data.model.Courses
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
    val pointsLine: String,
    /** "Eligible for <course>" when a course filter is applied, otherwise null. */
    val eligibilityLine: String? = null
)

data class MarketplaceUiState(
    val query: String = "",
    val selectedFilter: MarketplaceFilter = MarketplaceFilter.ALL,
    val selectedCourse: String? = null,
    val courseOptions: List<String> = Courses.all,
    val isCoursePickerOpen: Boolean = false,
    val events: List<EventCardUi> = emptyList(),
    val isLoading: Boolean = true
) {
    val resultsLabel: String
        get() {
            val heading = if (selectedCourse != null) "Qualifying events" else "Upcoming events"
            return "$heading · ${events.size} ${if (events.size == 1) "result" else "results"}"
        }
}

/** The parts of the screen the user controls, kept together so they can be combined in one step. */
private data class MarketplaceInputs(
    val query: String = "",
    val filter: MarketplaceFilter = MarketplaceFilter.ALL,
    val course: String? = null,
    val isCoursePickerOpen: Boolean = false
)

/**
 * Search, chip and course filter logic for the Discover events screen.
 * The screen only reads [uiState] and reports user actions back here.
 */
class MarketplaceViewModel(
    repository: EventRepository = SampleEventRepository,
    private val now: () -> LocalDateTime = { LocalDateTime.now() }
) : ViewModel() {

    private val inputs = MutableStateFlow(MarketplaceInputs())

    val uiState: StateFlow<MarketplaceUiState> =
        combine(repository.approvedEvents(), inputs) { events, input ->
            MarketplaceUiState(
                query = input.query,
                selectedFilter = input.filter,
                selectedCourse = input.course,
                isCoursePickerOpen = input.isCoursePickerOpen,
                events = filterEvents(events, input.query, input.filter, now(), input.course)
                    .map { toCardUi(it, input.course) },
                isLoading = false
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MarketplaceUiState())

    fun onQueryChange(value: String) { inputs.value = inputs.value.copy(query = value) }

    fun onFilterSelected(value: MarketplaceFilter) { inputs.value = inputs.value.copy(filter = value) }

    /** "Filter by course" or "Change" was tapped. */
    fun onOpenCoursePicker() { inputs.value = inputs.value.copy(isCoursePickerOpen = true) }

    fun onDismissCoursePicker() { inputs.value = inputs.value.copy(isCoursePickerOpen = false) }

    fun onCourseSelected(course: String) {
        inputs.value = inputs.value.copy(course = course, isCoursePickerOpen = false)
    }

    /** "Clear" was tapped on the applied filter. */
    fun onClearCourse() { inputs.value = inputs.value.copy(course = null) }

    companion object {
        private val dateFormat = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH)
        private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

        /**
         * Keeps upcoming events that match the search text, chip and (optional) course, soonest first.
         * With a course selected, only events awarding s-CPD points to that course remain.
         */
        fun filterEvents(
            events: List<Event>,
            query: String,
            filter: MarketplaceFilter,
            now: LocalDateTime,
            course: String? = null
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
                .filter { course == null || it.qualifiesFor(course) }
                .sortedBy { it.start }
        }

        /** Formats an event the way the Figma card shows it. */
        fun toCardUi(event: Event, course: String? = null) = EventCardUi(
            id = event.id,
            title = event.title,
            dateLine = "${event.start.format(dateFormat)} · ${event.start.format(timeFormat)}–${event.end.format(timeFormat)}",
            pointsLine = if (event.isFull) "${event.cpdPoints} s-CPD points · Event full"
            else "${event.cpdPoints} s-CPD points · ${event.placesLeft} ${if (event.placesLeft == 1) "place" else "places"} left",
            eligibilityLine = course?.let { "Eligible for $it" }
        )
    }
}
