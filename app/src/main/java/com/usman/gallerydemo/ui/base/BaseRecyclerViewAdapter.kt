package com.usman.gallerydemo.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {
    val VIEW_TYPE_EMPTY = 0
    val VIEW_TYPE_NORMAL = 1

    protected abstract fun createNormalItemViewHolder(parent: ViewGroup): T

    protected abstract fun createEmptyItemViewHolder(parent: ViewGroup): T

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        return when (viewType) {
            VIEW_TYPE_EMPTY -> createEmptyItemViewHolder(parent)
            else -> createNormalItemViewHolder(parent)
        }
    }

}