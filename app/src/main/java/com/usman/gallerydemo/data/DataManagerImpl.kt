package com.usman.gallerydemo.data

import com.usman.gallerydemo.utils.ResourceProvider
import javax.inject.Inject

class DataManagerImpl @Inject constructor(
    private val resourceProvider: ResourceProvider) : DataManager {

    override fun getResourceManager(): ResourceProvider {
        return resourceProvider
    }
}