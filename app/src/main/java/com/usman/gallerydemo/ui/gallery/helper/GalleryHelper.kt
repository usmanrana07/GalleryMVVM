package com.usman.gallerydemo.ui.gallery.helper

import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.SparseArray
import androidx.exifinterface.media.ExifInterface
import com.usman.gallerydemo.data.local.models.MediaFolder
import com.usman.gallerydemo.data.local.models.MediaItem
import com.usman.gallerydemo.utils.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GalleryHelper(private val context: Context) {


    /**
     * This method is used to update the width and height information
     * when that info is not available in Gallery. For example when
     * image is downloaded from any website. In those cases the resolution
     * info might be missing so we need to decode the bitmap.
     *
     * @param media Gallery media item
     */
    private fun decodeBitmapToGetOptions(media: MediaItem) {
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true

            //Returns null, sizes are in the options variable
            BitmapFactory.decodeFile(media.path, options)
            if (media.width <= 0) media.width = options.outWidth
            if (media.height <= 0) media.height = options.outHeight
            if (media.mimeType.isNullOrEmpty()) media.mimeType = options.outMimeType
            val exif = ExifInterface(media.path)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> media.orientation = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> media.orientation = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> media.orientation = 270
            }
        } catch (ignore: Exception) {
        }
    }

    suspend fun loadGalleryFolders(
        galleryMode: Int,
        grandFolderName: String,
    ): SparseArray<MediaFolder> =
        withContext(Dispatchers.IO) {

            val mFoldersList = SparseArray<MediaFolder>()
            try {
                val cursor: Cursor?
                // Return only video or image metadata.
                val uri: Uri = MediaStore.Files.getContentUri("external")
                val selection: String = when (galleryMode) {
                    GALLERY_IMAGE_AND_VIDEOS -> StringBuilder().append(MediaStore.Files.FileColumns.MEDIA_TYPE)
                        .append(" IN(")
                        .append(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
                        .append(",")
                        .append(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
                        .append(")").toString()
                    GALLERY_VIDEO -> StringBuilder().append(MediaStore.Files.FileColumns.MEDIA_TYPE)
                        .append("=")
                        .append(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO).toString()
                    GALLERY_IMAGE ->
                        StringBuilder().append(MediaStore.Files.FileColumns.MEDIA_TYPE).append("=")
                            .append(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE).toString()
                    else -> StringBuilder().append(MediaStore.Files.FileColumns.MEDIA_TYPE)
                        .append("=")
                        .append(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE).toString()
                }
                val projection = arrayOf(
                    MediaStore.MediaColumns._ID,
                    MediaStore.MediaColumns.TITLE,
                    MediaStore.MediaColumns.MIME_TYPE,
                    MediaStore.MediaColumns.WIDTH,
                    MediaStore.MediaColumns.HEIGHT,
                    MediaStore.MediaColumns.SIZE,
                    MediaStore.MediaColumns.BUCKET_ID,
                    MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.MediaColumns.ORIENTATION,
                )
                val orderBy = MediaStore.MediaColumns.DATE_TAKEN
                cursor = context.contentResolver?.query(
                    uri,
                    projection,
                    selection,
                    null,
                    "$orderBy DESC"
                )
                var grandFolder: MediaFolder? = null
                // to show this folder at top we need to assign it lowest value
                // as in gallery some folders have bucketId in -ve already
                grandFolder = MediaFolder(Int.MIN_VALUE, title = grandFolderName)
                mFoldersList.put(grandFolder.id, grandFolder)
                cursor?.let {
                    while (cursor.moveToNext()) {

//                    for (int i = 0; i < cursor.getColumnCount(); i++)
//                        Log.e("usm_gallery_column_" + i, "name_" + cursor.getColumnName(i) + " ,value= " + cursor.getString(i));
                        val bucketId =
                            cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns.BUCKET_ID))
                        val galleryMedia: MediaItem? = cursorToGalleryMedia(cursor)

                        galleryMedia?.let {
                            if (it.width < 1) {
                                decodeBitmapToGetOptions(it)
                            }
                            grandFolder.mediaList.add(it)

                            mFoldersList[bucketId, null]?.mediaList?.add(galleryMedia)
                                ?: kotlin.run {
                                    val galleryFolder = MediaFolder(
                                        bucketId,
                                        title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                                    )
                                    galleryFolder.mediaList.add(galleryMedia)
                                    mFoldersList.put(bucketId, galleryFolder)
                                }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            AppLogger.d("usm_gallery_folders", "size= " + mFoldersList.size())
            mFoldersList
        }

    private fun cursorToGalleryMedia(cursor: Cursor?): MediaItem? {
        return cursor?.let {
            MediaItem(
                id = it.getInt(it.getColumnIndex(MediaStore.Images.Media._ID)),
                width = it.getInt(it.getColumnIndex(MediaStore.Images.Media.WIDTH)),
                height = it.getInt(it.getColumnIndex(MediaStore.Images.Media.HEIGHT)),
                path = Uri.withAppendedPath(
                    MediaStore.Files.getContentUri("external"),
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)).toString()
                ).toString(),
                size = it.getLong(it.getColumnIndex(MediaStore.Images.Media.SIZE)),
                orientation = it.getInt(it.getColumnIndex(MediaStore.Images.Media.ORIENTATION))
            ).apply {
                mimeType = it.getString(it.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
            }
        } ?: run { null }
    }

    companion object {
        const val GALLERY_IMAGE = 0
        const val GALLERY_VIDEO = 1
        const val GALLERY_IMAGE_AND_VIDEOS = 2
    }
}