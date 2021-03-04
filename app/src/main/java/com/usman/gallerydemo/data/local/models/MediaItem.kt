package com.usman.gallerydemo.data.local.models

import kotlin.properties.Delegates

data class MediaItem(
    val id: Int,
    var path: String,
    var width: Int,
    var height: Int,
    var size: Long,
    var orientation: Int? = null
) {
    var isVideo by Delegates.notNull<Boolean>()
    var mimeType: String? = null
        set(value) {
            isVideo = value?.startsWith("video", true) ?: false
            field = value
        }

}