package com.usman.gallerydemo.ui.gallery.adapter.folders

import com.usman.gallerydemo.data.local.models.MediaFolder

class GalleryFolderItemViewModel(
    private val mediaFolder: MediaFolder,
    private val onItemClickLambda: ((MediaFolder) -> Unit)?
) {
    var thumbnailUrl = mediaFolder.mediaList.takeIf { it.size > 0 }?.let { it[0].path }
    var title = mediaFolder.title
    var count = mediaFolder.mediaList.size
    var isSelected = mediaFolder.isSelected


    fun onItemClicked() {
        onItemClickLambda?.invoke(mediaFolder)
    }

}