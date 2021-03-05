package com.usman.gallerydemo.ui.dashboard

import com.usman.gallerydemo.utils.GalleryModes

interface DashBoardNavigator {

    fun openGallery(@GalleryModes mode: Int)

}