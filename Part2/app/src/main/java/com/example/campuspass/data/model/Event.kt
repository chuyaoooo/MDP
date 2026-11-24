package com.example.campuspass.data.model

import java.time.LocalDateTime

/** Type of activity, used by the marketplace filter chips. */
enum class EventCategory(val label: String) {
    CAREERS("Careers"),
    SKILLS("Skills"),
    COMMUNITY("Community")
}

/** An approved s-CPD event that students can browse and book. */
data class Event(
    val id: String,
    val title: String,
    val category: EventCategory,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val cpdPoints: Int,
    val capacity: Int,
    val bookedCount: Int,
    val location: String,
    val organiser: String,
    val description: String,
    /** Courses whose students earn s-CPD points here. Empty means every course qualifies. */
    val eligibleCourses: List<String> = emptyList()
) {
    val placesLeft: Int get() = (capacity - bookedCount).coerceAtLeast(0)
    val isFull: Boolean get() = placesLeft == 0
    val isOpenToAllCourses: Boolean get() = eligibleCourses.isEmpty()

    /** True if a student on [course] can earn points at this event. */
    fun qualifiesFor(course: String): Boolean = isOpenToAllCourses || course in eligibleCourses
}
