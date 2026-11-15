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
    val eligibleCourses: List<String> = emptyList()
) {
    val placesLeft: Int get() = (capacity - bookedCount).coerceAtLeast(0)
    val isFull: Boolean get() = placesLeft == 0
}
