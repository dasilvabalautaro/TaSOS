package com.globalhiddenodds.tasos.models.persistent.database.data

import android.arch.persistence.room.ColumnInfo

data class SSetContacts(@ColumnInfo(name = "source") var source: String) {
}