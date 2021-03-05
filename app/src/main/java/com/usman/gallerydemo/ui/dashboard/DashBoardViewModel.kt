package com.usman.gallerydemo.ui.dashboard

import com.usman.gallerydemo.data.DataManager
import com.usman.gallerydemo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    dataManager: DataManager,
) :
    BaseViewModel<DashBoardNavigator>(dataManager) {

    fun onOptionClicked(galleryMode: Int) {
        getNavigator()?.openGallery(galleryMode)
    }
}