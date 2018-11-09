package com.globalhiddenodds.tasos.domain.data

import com.globalhiddenodds.tasos.extension.empty

data class GroupMessage(var source: String,
                        var type: Int,
                        var quantity: Int) {
    companion object {
        fun empty() = GroupMessage(String.empty(), 0, 0)
    }
}