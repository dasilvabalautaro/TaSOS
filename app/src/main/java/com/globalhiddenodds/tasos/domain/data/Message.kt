package com.globalhiddenodds.tasos.domain.data

import com.globalhiddenodds.tasos.extension.empty

data class Message(var source: String,
                   var message: String,
                   var dateMessage: Long,
                   var type: Int,
                   var state: Int) {
    companion object {
        fun empty() = Message(String.empty(),String.empty(), 0,
                0, 0)
    }
}