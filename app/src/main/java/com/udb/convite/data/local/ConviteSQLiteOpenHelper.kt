package com.udb.convite.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.udb.convite.data.EventModel

class ConviteSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "convite_local.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_EVENTS = "events"

        private const val COLUMN_ID = "id"
        private const val COLUMN_USER_ID = "userId"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_REMINDER_TIME = "reminderTime"
        private const val COLUMN_REMINDERS = "reminders"
        private const val COLUMN_REMINDER_ACTIVE = "reminderActive"
        private const val COLUMN_GROUP_TAG = "groupTag"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_EVENTS (" +
                "$COLUMN_ID TEXT PRIMARY KEY, " +
                "$COLUMN_USER_ID TEXT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_TIME TEXT, " +
                "$COLUMN_LOCATION TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_CATEGORY TEXT, " +
                "$COLUMN_REMINDER_TIME TEXT, " +
                "$COLUMN_REMINDERS TEXT, " +
                "$COLUMN_REMINDER_ACTIVE INTEGER, " +
                "$COLUMN_GROUP_TAG TEXT)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTS")
        onCreate(db)
    }

    fun insertEvent(event: EventModel) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, event.id.ifEmpty { System.currentTimeMillis().toString() })
            put(COLUMN_USER_ID, event.userId)
            put(COLUMN_TITLE, event.title)
            put(COLUMN_DATE, event.date)
            put(COLUMN_TIME, event.time)
            put(COLUMN_LOCATION, event.location)
            put(COLUMN_DESCRIPTION, event.description)
            put(COLUMN_CATEGORY, event.category)
            put(COLUMN_REMINDER_TIME, event.reminderTime)
            put(COLUMN_REMINDERS, event.reminders.joinToString(","))
            put(COLUMN_REMINDER_ACTIVE, if (event.reminderActive) 1 else 0)
            put(COLUMN_GROUP_TAG, event.groupTag)
        }
        db.insertWithOnConflict(TABLE_EVENTS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
    }

    fun getEvents(userId: String): List<EventModel> {
        val events = mutableListOf<EventModel>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_EVENTS,
            null,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val uId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
                val location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
                val reminderTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_TIME))
                val remindersStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REMINDERS))
                val reminderActive = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_ACTIVE)) == 1
                val groupTag = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GROUP_TAG))

                val remindersList = if (!remindersStr.isNullOrBlank()) remindersStr.split(",") else emptyList()

                events.add(
                    EventModel(
                        id = id,
                        userId = uId,
                        title = title,
                        date = date,
                        time = time,
                        location = location,
                        description = description,
                        category = category,
                        reminderTime = reminderTime,
                        reminders = remindersList,
                        reminderActive = reminderActive,
                        groupTag = groupTag
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return events
    }

    fun deleteEvent(eventId: String) {
        val db = writableDatabase
        db.delete(TABLE_EVENTS, "$COLUMN_ID = ?", arrayOf(eventId))
        db.close()
    }
}
