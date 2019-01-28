package com.globalhiddenodds.tasos.models.persistent.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.globalhiddenodds.tasos.models.persistent.database.data.MessageData
import com.globalhiddenodds.tasos.models.persistent.database.interfaces.MessageDataDao

@Database(entities = [MessageData::class],
        version = 1, exportSchema = false)

abstract class AppDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDataDao
}