package com.usman.gallerydemo.ui.gallery.adapter.media

import com.usman.gallerydemo.data.local.models.MediaItem

class GalleryItemViewModel(
    private val mediaItem: MediaItem,
    private val onItemClickLambda: ((MediaItem) -> Unit)?
) {
    var thumbnailUrl = mediaItem.path
    var isVideo = mediaItem.isVideo


    fun onItemClicked() {
        onItemClickLambda?.invoke(mediaItem)
    }

}