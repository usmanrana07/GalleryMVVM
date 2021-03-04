package com.usman.gallerydemo.data.local.models

data class MediaFolder(
    var id: Int,
    var title: String,
    val thumbnail: String? = null,
    val isSelected: Boolean = false,
    val mediaList: MutableList<MediaItem> = arrayListOf(),
)