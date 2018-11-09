package com.globalhiddenodds.tasos.models.persistent.database.data

import android.arch.persistence.room.ColumnInfo

data class SSetMessages(@ColumnInfo(name = "message") var message: String,
                        @ColumnInfo(name = "dateMessage") var dateMessage: Long) {
}