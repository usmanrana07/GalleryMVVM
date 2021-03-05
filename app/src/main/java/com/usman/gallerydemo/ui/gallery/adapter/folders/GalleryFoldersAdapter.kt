package com.usman.gallerydemo.ui.gallery.adapter.folders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.usman.gallerydemo.R
import com.usman.gallerydemo.data.local.models.MediaFolder
import com.usman.gallerydemo.databinding.ItemEmptyGalleryBinding
import com.usman.gallerydemo.databinding.ItemGalleryFolderViewBinding
import com.usman.gallerydemo.ui.base.BaseRecyclerViewAdapter
import com.usman.gallerydemo.ui.base.BaseViewHolder
import com.usman.gallerydemo.ui.base.EmptyItemViewHolder
import com.usman.gallerydemo.utils.AppLogger
import javax.inject.Inject

class GalleryFoldersAdapter @Inject constructor(val context: FragmentActivity) :
    BaseRecyclerViewAdapter<BaseViewHolder>(),
    MediaFolderViewHolderInterface {
    private var foldersList: MutableList<MediaFolder> = arrayListOf()

    override fun getItemCount(): Int {
        return foldersList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (foldersList.size) {
            0 -> {
                viewTypeEmpty
            }
            else -> viewTypeNormal
        }
    }

    fun getListSize(): Int = foldersList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun createNormalItemViewHolder(parent: ViewGroup): BaseViewHolder {
        val itemBinding =
            ItemGalleryFolderViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        AppLogger.d("usm_gallery_folder_vh", "createNormalItemViewHolder")
        return MediaFolderItemViewHolder(itemBinding, this)
    }

    override fun createEmptyItemViewHolder(parent: ViewGroup): BaseViewHolder {
        return EmptyItemViewHolder(
            ItemEmptyGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            MutableLiveData(parent.context.getString(R.string.message_no_folder)),
            R.drawable.ic_image
        )
    }


    fun clearItems() {
        foldersList.clear()
        notifyDataSetChanged()
    }

    fun addItems(items: List<MediaFolder>) {
        foldersList.addAll(items)
        notifyDataSetChanged()
    }

    private var selectionCall: ((Int, String) -> Unit)? = null
    fun setFolderSelectionListenerLambda(selectionCall: (Int, String) -> Unit) {
        this.selectionCall = selectionCall
    }

    override fun getMediaFolder(position: Int): MediaFolder {
        return foldersList[position]
    }

    override fun onItemClicked(mediaFolder: MediaFolder) {
        selectionCall?.invoke(mediaFolder.id, mediaFolder.title)
    }

    class MediaFolderItemViewHolder(
        private val itemBinding: ItemGalleryFolderViewBinding,
        private val itemListener: MediaFolderViewHolderInterface,
    ) : BaseViewHolder(itemBinding.root) {
        private lateinit var mediaFolder: MediaFolder

        override fun onBind(position: Int) {

            mediaFolder = itemListener.getMediaFolder(position)
            itemBinding.viewModel =
                GalleryFolderItemViewModel(mediaFolder) { folder ->
                    itemListener.onItemClicked(folder)
                }
            itemBinding.executePendingBindings()
        }
    }

}


interface MediaFolderViewHolderInterface {
    fun getMediaFolder(position: Int): MediaFolder
    fun onItemClicked(mediaFolder: MediaFolder)
}

