package com.usman.gallerydemo.data

import com.usman.gallerydemo.utils.ResourceProvider

interface DataManager {

    fun getResourceManager(): ResourceProvider
}