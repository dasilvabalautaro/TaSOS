package com.globalhiddenodds.tasos.tools

import android.content.Context
import com.globalhiddenodds.tasos.extension.networkInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHandler @Inject constructor(private val context: Context) {
    val isConnected get() = context.networkInfo?.isConnectedOrConnecting
}