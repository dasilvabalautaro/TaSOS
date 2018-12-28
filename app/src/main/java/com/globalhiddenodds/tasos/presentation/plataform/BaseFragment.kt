package com.globalhiddenodds.tasos.presentation.plataform


import android.support.v4.app.Fragment
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.R

import com.globalhiddenodds.tasos.di.ApplicationComponent
import com.globalhiddenodds.tasos.extension.appContext
import com.globalhiddenodds.tasos.extension.viewContainer
import kotlinx.android.synthetic.main.progress.*
import com.globalhiddenodds.tasos.R.color
import com.globalhiddenodds.tasos.models.exception.Failure
import io.reactivex.disposables.CompositeDisposable

import javax.inject.Inject



abstract class BaseFragment: Fragment() {
    abstract fun layoutId(): Int
    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal var disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(layoutId(), container, false)

    open fun onBackPressed() {}

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun showProgress() = progressStatus(View.VISIBLE)

    internal fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) =
            with(activity) { if (this is BaseActivity)
                this.progress.visibility = viewStatus }

    internal fun notify(@StringRes message: Int) =
            Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT).show()

    internal fun notifyWithAction(@StringRes message: Int, @StringRes actionText: Int) {
        val snackBar = Snackbar.make(viewContainer, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setActionTextColor(ContextCompat.getColor(appContext,
                color.colorTextPrimary))
        snackBar.show()
    }
    abstract fun renderFailure(@StringRes message: Int)

    internal fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is Failure.DatabaseError -> renderFailure(R.string.failure_database_error)
        }
    }

    internal fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


    internal fun hideKeyboard(activity: BaseActivity) {
        val view = activity.currentFocus?: View(activity)
        val imm = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

}