package com.usman.gallerydemo.ui.base

import androidx.lifecycle.MutableLiveData
import com.usman.gallerydemo.databinding.ItemEmptyGalleryBinding

class EmptyItemViewHolder(
    private val binding: ItemEmptyGalleryBinding,
    val text: MutableLiveData<String>,
    val icon: Int
) :
    BaseViewHolder(binding.root) {
    override fun onBind(position: Int) {
        binding.viewModel = EmptyItemViewModel(text, icon)
    }
}