package com.usman.gallerydemo.ui.gallery

import com.usman.gallerydemo.ui.gallery.helper.GalleryHelper

interface GalleryNavigator {

    val galleryHelper : GalleryHelper

    fun loadGallery()

    fun hasStoragePermission(): Boolean

    fun switchGalleryMode()

}