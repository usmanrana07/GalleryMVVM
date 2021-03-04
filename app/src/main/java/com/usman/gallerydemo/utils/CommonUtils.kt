package com.usman.gallerydemo.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.SparseArray
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import java.io.File
import java.util.*

object CommonUtils {

    fun getFileExtension(path: String): String {
        return path.substring(path.lastIndexOf('.'))
    }

    fun getMimeType(context: Context, file: File): String {
        val mimeType: String = if (file.toUri().scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            val cr = context.contentResolver
            cr.getType(file.toUri()).toString()
        } else {
            val extension = MimeTypeMap.getFileExtensionFromUrl(file.toUri().toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                extension.toLowerCase(
                    Locale.ROOT
                )
            ).toString()
        }
        AppLogger.d("file_mimeType", "type = $mimeType")

        return mimeType
    }

    fun getFileDuration(videoFile: File?, activity: Activity?): Long {
        var timeInMillis: Long
        try {
            val retriever = MediaMetadataRetriever()
            //use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(activity, Uri.fromFile(videoFile))
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            timeInMillis = time!!.toLong()
            retriever.release()
        } catch (ignore: Exception) {
            timeInMillis = -1
        }
        AppLogger.d("usm_camera_video_duration", "millis= $timeInMillis")
        return timeInMillis
    }

    fun <C> asList(sparseArray: SparseArray<C>): MutableList<C> {
        val arrayList = arrayListOf<C>()
        for (i in 0 until sparseArray.size()) arrayList.add(sparseArray.valueAt(i))
        return arrayList
    }
}