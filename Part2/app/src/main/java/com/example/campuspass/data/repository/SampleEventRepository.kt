package com.example.campuspass.data.repository

import com.example.campuspass.data.model.Event
import com.example.campuspass.data.model.EventCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Temporary in-memory events matching the Figma wireframes.
 * Dates are relative to today so the "This week" filter always has something to show.
 * TODO: replace with the Firebase-backed repository.
 */
object SampleEventRepository : EventRepository {

    private fun at(daysFromToday: Long, hour: Int, minute: Int = 0): LocalDateTime =
        LocalDateTime.of(LocalDate.now().plusDays(daysFromToday), LocalTime.of(hour, minute))

    val events: List<Event> = listOf(
        Event(
            id = "career-ready",
            title = "Career Ready Workshop",
            category = EventCategory.CAREERS,
            start = at(2, 14), end = at(2, 15, 30),
            cpdPoints = 10, capacity = 40, bookedCount = 28,
            location = "Block F1 · Room F1A23",
            organiser = "Careers Service",
            description = "Build a CV that stands out and practise interview answers with recruiters.",
            eligibleCourses = listOf("BA Business Management", "BA Marketing", "BSc Accounting and Finance")
        ),
        Event(
            id = "leadership-lab",
            title = "Leadership Lab",
            category = EventCategory.SKILLS,
            start = at(2, 15), end = at(2, 16),
            cpdPoints = 15, capacity = 20, bookedCount = 12,
            location = "Trent Building · Room TB102",
            organiser = "Student Leaders Club",
            description = "Hands-on activities on leading a team, giving feedback and running meetings.",
            eligibleCourses = listOf("BA Business Management", "BSc Computer Science", "BEng Electrical Engineering")
        ),
        Event(
            id = "sustainable-futures",
            title = "Sustainable Futures Talk",
            category = EventCategory.COMMUNITY,
            start = at(4, 11), end = at(4, 12),
            cpdPoints = 5, capacity = 60, bookedCount = 36,
            location = "Great Hall",
            organiser = "Green Society",
            description = "A guest talk on sustainability careers and how students can get involved.",
            eligibleCourses = listOf("BSc Computer Science", "BEng Electrical Engineering")
        ),
        Event(
            id = "data-viz",
            title = "Data Visualisation Bootcamp",
            category = EventCategory.SKILLS,
            start = at(10, 10), end = at(10, 13),
            cpdPoints = 20, capacity = 25, bookedCount = 25,
            location = "Block B · Computer Lab 2",
            organiser = "Computer Science Society",
            description = "Turn raw data into clear charts using Python.",
            eligibleCourses = listOf("BSc Computer Science", "BSc Software Engineering")
        )
    )

    override fun approvedEvents(): Flow<List<Event>> = flowOf(events)
}
