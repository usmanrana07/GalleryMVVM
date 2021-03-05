package com.usman.gallerydemo.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {
    val viewTypeEmpty = 0
    val viewTypeNormal = 1

    protected abstract fun createNormalItemViewHolder(parent: ViewGroup): T

    protected abstract fun createEmptyItemViewHolder(parent: ViewGroup): T

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        return when (viewType) {
            viewTypeEmpty -> createEmptyItemViewHolder(parent)
            else -> createNormalItemViewHolder(parent)
        }
    }

}