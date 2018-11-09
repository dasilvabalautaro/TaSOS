package com.globalhiddenodds.tasos.models.persistent.database.data

import android.arch.persistence.room.ColumnInfo

data class SSetNewMessageQuantity(@ColumnInfo(name = "source") var source: String,
                                  @ColumnInfo(name = "quantity") var quantity: Int) {
}