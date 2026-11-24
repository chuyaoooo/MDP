package com.example.campuspass.ui.student.marketplace

import com.example.campuspass.data.model.Event
import com.example.campuspass.data.model.EventCategory
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class MarketplaceFilterTest {

    private val now = LocalDateTime.of(2026, 11, 16, 9, 0)

    private fun event(
        id: String,
        title: String,
        category: EventCategory,
        daysAhead: Long,
        booked: Int = 0,
        courses: List<String> = emptyList()
    ) = Event(
        id = id, title = title, category = category,
        start = now.plusDays(daysAhead), end = now.plusDays(daysAhead).plusHours(1),
        cpdPoints = 10, capacity = 10, bookedCount = booked,
        location = "Room", organiser = "Club", description = "",
        eligibleCourses = courses
    )

    private val business = "BA Business Management"
    private val compSci = "BSc Computer Science"

    private val careers = event("1", "Career Ready Workshop", EventCategory.CAREERS, 2, courses = listOf(business))
    private val skills = event("2", "Leadership Lab", EventCategory.SKILLS, 1) // open to every course
    private val later = event("3", "Data Bootcamp", EventCategory.SKILLS, 10, courses = listOf(compSci))
    private val past = event("4", "Old Talk", EventCategory.COMMUNITY, -3)
    private val all = listOf(careers, skills, later, past)

    private fun ids(result: List<Event>) = result.map { it.id }

    @Test
    fun all_hidesPastEvents_andSortsSoonestFirst() {
        assertEquals(listOf("2", "1", "3"), ids(MarketplaceViewModel.filterEvents(all, "", MarketplaceFilter.ALL, now)))
    }

    @Test
    fun careersChip_showsOnlyCareerEvents() {
        assertEquals(listOf("1"), ids(MarketplaceViewModel.filterEvents(all, "", MarketplaceFilter.CAREERS, now)))
    }

    @Test
    fun thisWeekChip_excludesEventsMoreThan7DaysAway() {
        assertEquals(listOf("2", "1"), ids(MarketplaceViewModel.filterEvents(all, "", MarketplaceFilter.THIS_WEEK, now)))
    }

    @Test
    fun search_isCaseInsensitive_andCombinesWithChip() {
        assertEquals(listOf("3"), ids(MarketplaceViewModel.filterEvents(all, "BOOT", MarketplaceFilter.SKILLS, now)))
    }

    @Test
    fun search_withNoMatch_returnsEmptyList() {
        assertEquals(emptyList<String>(), ids(MarketplaceViewModel.filterEvents(all, "zzz", MarketplaceFilter.ALL, now)))
    }

    @Test
    fun cardText_matchesFigmaFormat() {
        val card = MarketplaceViewModel.toCardUi(careers)
        assertEquals("18 Nov 2026 · 09:00–10:00", card.dateLine)
        assertEquals("10 s-CPD points · 10 places left", card.pointsLine)
        assertEquals(null, card.eligibilityLine)
    }

    @Test
    fun fullEvent_saysEventFull() {
        val full = event("5", "Full Event", EventCategory.SKILLS, 1, booked = 10)
        assertEquals("10 s-CPD points · Event full", MarketplaceViewModel.toCardUi(full).pointsLine)
    }

    @Test
    fun courseFilter_keepsOnlyQualifyingEvents() {
        assertEquals(listOf("2", "1"), ids(MarketplaceViewModel.filterEvents(all, "", MarketplaceFilter.ALL, now, business)))
    }

    @Test
    fun courseFilter_includesEventsOpenToAllCourses() {
        assertEquals(listOf("2", "3"), ids(MarketplaceViewModel.filterEvents(all, "", MarketplaceFilter.ALL, now, compSci)))
    }

    @Test
    fun courseFilter_combinesWithChip() {
        assertEquals(listOf("1"), ids(MarketplaceViewModel.filterEvents(all, "", MarketplaceFilter.CAREERS, now, business)))
    }

    @Test
    fun cardWithCourse_showsEligibilityLine() {
        assertEquals("Eligible for $business", MarketplaceViewModel.toCardUi(careers, business).eligibilityLine)
    }

    @Test
    fun resultsLabel_switchesToQualifyingWhenCourseApplied() {
        val cards = listOf(MarketplaceViewModel.toCardUi(careers, business), MarketplaceViewModel.toCardUi(skills, business))
        assertEquals("Qualifying events · 2 results", MarketplaceUiState(selectedCourse = business, events = cards).resultsLabel)
        assertEquals("Upcoming events · 1 result", MarketplaceUiState(events = cards.take(1)).resultsLabel)
    }
}
