package com.usman.gallerydemo.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.usman.gallerydemo.data.DataManager
import java.lang.ref.WeakReference

open class BaseViewModel<N>(val dataManager: DataManager) : ViewModel() {
    val isLoading = MutableLiveData(false)
    private var navigator: WeakReference<N>? = null

    fun getNavigator(): N? {
        return navigator?.get()
    }

    fun setNavigator(navigator: N) {
        this.navigator = WeakReference(navigator)
    }

}