package com.udb.convite.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference.child("events")

    val currentUserId: String?
        get() = auth.currentUser?.uid

    fun getEvents(): Flow<List<EventModel>> = callbackFlow {
        val uid = currentUserId
        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<EventModel>()
                for (child in snapshot.children) {
                    val event = child.getValue(EventModel::class.java)
                    if (event != null && event.userId == uid) {
                        list.add(event)
                    }
                }
                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.addValueEventListener(listener)
        awaitClose { database.removeEventListener(listener) }
    }

    suspend fun addEvent(event: EventModel) {
        val uid = currentUserId ?: return
        val id = database.push().key ?: return
        val newEvent = event.copy(id = id, userId = uid)
        database.child(id).setValue(newEvent).await()
    }

    suspend fun updateEvent(event: EventModel) {
        if (event.id.isNotEmpty()) {
            database.child(event.id).setValue(event).await()
        }
    }

    suspend fun deleteEvent(eventId: String) {
        if (eventId.isNotEmpty()) {
            database.child(eventId).removeValue().await()
        }
    }
}
