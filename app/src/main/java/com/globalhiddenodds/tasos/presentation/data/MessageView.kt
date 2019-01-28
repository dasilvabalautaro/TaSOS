package com.globalhiddenodds.tasos.presentation.data

import android.os.Parcel
import com.globalhiddenodds.tasos.presentation.plataform.KParcelable
import com.globalhiddenodds.tasos.presentation.plataform.parcelableCreator
import com.globalhiddenodds.tasos.presentation.plataform.readBoolean
import com.globalhiddenodds.tasos.presentation.plataform.writeBoolean

data class MessageView(var source: String,
                       var target: String,
                       var message: String,
                       var dateMessage: Long,
                       var type: Int,
                       var state: Int,
                       var who: Boolean): KParcelable {

    companion object {
        @JvmField val CREATOR = parcelableCreator(
                ::MessageView)
    }

    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString(),
            parcel.readString(), parcel.readLong(), parcel.readInt(),
            parcel.readInt(), parcel.readBoolean())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeString(source)
            writeString(target)
            writeString(message)
            writeLong(dateMessage)
            writeInt(type)
            writeInt(state)
            writeBoolean(who)

        }
    }
}