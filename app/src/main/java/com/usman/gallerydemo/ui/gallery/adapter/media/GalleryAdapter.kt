package com.usman.gallerydemo.ui.gallery.adapter.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.usman.gallerydemo.R
import com.usman.gallerydemo.data.local.models.MediaItem
import com.usman.gallerydemo.databinding.ItemEmptyGalleryBinding
import com.usman.gallerydemo.databinding.ItemGalleryViewBinding
import com.usman.gallerydemo.ui.base.BaseRecyclerViewAdapter
import com.usman.gallerydemo.ui.base.BaseViewHolder
import com.usman.gallerydemo.ui.base.EmptyItemViewHolder
import com.usman.gallerydemo.utils.AppLogger
import com.usman.gallerydemo.utils.showToast
import javax.inject.Inject

class GalleryAdapter @Inject constructor(val context: FragmentActivity) :
    BaseRecyclerViewAdapter<BaseViewHolder>(), MediaItemViewHolderInterface {
    private var mediaList: MutableList<MediaItem> = ArrayList()

    override fun onItemClicked(mediaItem: MediaItem) {
        context.showToast("Clicked ${mediaItem.path}")
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (mediaList.size) {
            0 -> {
                VIEW_TYPE_EMPTY
            }
            else -> VIEW_TYPE_NORMAL
        }
    }

    fun getListSize(): Int = mediaList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun createNormalItemViewHolder(parent: ViewGroup): BaseViewHolder {
        val itemBinding =
            ItemGalleryViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        itemBinding.lifecycleOwner = context
        return MediaItemViewHolder(itemBinding, this)
    }

    override fun createEmptyItemViewHolder(parent: ViewGroup): BaseViewHolder {
        return EmptyItemViewHolder(
            ItemEmptyGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).apply {
                lifecycleOwner = context
            },
            MutableLiveData(parent.context.getString(R.string.message_empty_gallery)),
            R.drawable.ic_image
        )
    }


    fun clearItems() {
        mediaList.clear()
        notifyDataSetChanged()
    }

    fun addItems(items: List<MediaItem>) {
        mediaList.addAll(items)
        AppLogger.d("usm_gallery_folder_add", "items= ${items.size}")
        notifyDataSetChanged()
    }

    override fun getMediaItem(position: Int): MediaItem {
        return mediaList[position]
    }


    class MediaItemViewHolder(
        private val itemBinding: ItemGalleryViewBinding,
        private val itemListener: MediaItemViewHolderInterface,
        private val showSelection: Boolean = true,
    ) : BaseViewHolder(itemBinding.root) {
        private lateinit var mediaItem: MediaItem

        override fun onBind(position: Int) {

            mediaItem = itemListener.getMediaItem(position)

            itemBinding.viewModel =
                GalleryItemViewModel(mediaItem) {
                    itemListener.onItemClicked(it)
                }
            itemBinding.executePendingBindings()
        }
    }

}


interface MediaItemViewHolderInterface {
    fun getMediaItem(position: Int): MediaItem
    fun onItemClicked(mediaItem: MediaItem)
}

