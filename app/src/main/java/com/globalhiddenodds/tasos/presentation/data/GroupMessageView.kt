package com.globalhiddenodds.tasos.presentation.data

import android.os.Parcel
import com.globalhiddenodds.tasos.presentation.plataform.KParcelable
import com.globalhiddenodds.tasos.presentation.plataform.parcelableCreator

data class GroupMessageView(var source: String,
                            var type: Int,
                            var quantity: Int): KParcelable {

    companion object {
        @JvmField val CREATOR = parcelableCreator(
                ::GroupMessageView)
    }

    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeString(source)
            writeInt(type)
            writeInt(quantity)

        }
    }
}