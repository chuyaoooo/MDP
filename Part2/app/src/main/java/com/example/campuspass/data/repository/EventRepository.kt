package com.example.campuspass.data.repository

import com.example.campuspass.data.model.Event
import kotlinx.coroutines.flow.Flow

/**
 * Where the app gets approved events from.
 * The UI only talks to this interface, so the sample data below can be swapped
 * for a Firebase implementation without changing any screen.
 */
interface EventRepository {
    /** Approved events that are still upcoming, updated live. */
    fun approvedEvents(): Flow<List<Event>>
}
