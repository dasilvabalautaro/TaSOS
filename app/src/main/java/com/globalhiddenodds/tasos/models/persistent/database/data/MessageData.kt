package com.globalhiddenodds.tasos.models.persistent.database.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "messageData")
data class MessageData(@PrimaryKey(autoGenerate = true) var id: Int,
                       @ColumnInfo(name = "source") var source: String,
                       @ColumnInfo(name = "target") var target: String,
                       @ColumnInfo(name = "message") var message: String,
                       @ColumnInfo(name = "dateMessage") var dateMessage: Long,
                       @ColumnInfo(name = "type") var type: Int,
                       @ColumnInfo(name = "state") var state: Int,
                       @ColumnInfo(name = "who") var who: Boolean) {

    constructor(): this(0,"", "", "",
            0, 1, 1, true)
}