package com.globalhiddenodds.tasos.presentation.plataform

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.globalhiddenodds.tasos.models.exception.Failure

abstract class BaseViewModel: ViewModel() {
    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}