package com.usman.gallerydemo.ui.gallery

import android.util.SparseArray
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.usman.gallerydemo.R
import com.usman.gallerydemo.data.DataManager
import com.usman.gallerydemo.data.local.models.MediaFolder
import com.usman.gallerydemo.data.local.models.MediaItem
import com.usman.gallerydemo.ui.base.BaseViewModel
import com.usman.gallerydemo.utils.AppLogger
import com.usman.gallerydemo.utils.CommonUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    dataManager: DataManager,
) :
    BaseViewModel<GalleryNavigator>(dataManager) {

    private var mFoldersList: SparseArray<MediaFolder>? = null
    val hasGalleryPermission = MutableLiveData(true)
    var galleryFoldersLiveDataList: MutableLiveData<MutableList<MediaFolder>>? = null
    var galleryMediaLiveDataList: MutableLiveData<MutableList<MediaItem>?>? = null
    val selectedFolderName = MutableLiveData<String>()


    fun initLists() {
        galleryFoldersLiveDataList = MutableLiveData<MutableList<MediaFolder>>()
        galleryMediaLiveDataList = MutableLiveData()
        mFoldersList = SparseArray<MediaFolder>()
    }

    fun setSelectedFolder(bucketId: Int, title: String) {
        selectedFolderName.value = title
        addMediaItemsToList(getMediaList(bucketId))
    }

    private fun getMediaList(bucketId: Int): MutableList<MediaItem>? {
        return mFoldersList?.get(bucketId)?.mediaList
    }

    private fun addFolderItemsToList(items: MutableList<MediaFolder>) {
        AppLogger.d("usm_gallery_folder", "parsed items= ${items.size}")
        galleryFoldersLiveDataList?.value = items
    }

    private fun addMediaItemsToList(items: MutableList<MediaItem>?) {
        galleryMediaLiveDataList?.value = items ?: arrayListOf()
    }

    fun foldersCount(): Int {
        return mFoldersList?.size() ?: 0
    }

    fun loadGalleryMedia(
        galleryMode: Int,
        grandFolderName: String = dataManager.getResourceManager().getString(
            R.string.camera_roll
        ),
    ) {

        isLoading.value = true
        viewModelScope.launch(Dispatchers.Main)
        {

            mFoldersList =
                getNavigator()?.galleryHelper?.loadGalleryFolders(galleryMode, grandFolderName)
            mFoldersList?.let {
                addFolderItemsToList(CommonUtils.asList(it))
                if (it.size() > 0) {
                    // set Grand Folder (Camera Roll) as selected by default
                    val galleryFolder: MediaFolder = it.valueAt(0)
                    setSelectedFolder(galleryFolder.id, galleryFolder.title)
                    AppLogger.d(
                        "usm_gallery_folder_data",
                        "title= ${galleryFolder.title} , media= ${galleryFolder.mediaList.size}"
                    )
                }
            }

            isLoading.value = false
        }

    }

    fun onBackClicked() {
        getNavigator()?.close()
    }
    fun onChangeTypeClicked() {
        getNavigator()?.switchGalleryMode()
    }

    fun onPermissionAllowClicked() {
        getNavigator()?.loadGallery()
    }

}