package com.globalhiddenodds.tasos.models.interfaces

import com.globalhiddenodds.tasos.models.data.User
import io.reactivex.Observable

interface IAuthentication {
    fun start()
    fun getCurrentUser(user: User): Observable<Boolean>
    fun createUser(user: User): Observable<Boolean>
}