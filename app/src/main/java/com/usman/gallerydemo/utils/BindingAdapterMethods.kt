package com.usman.gallerydemo.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.usman.gallerydemo.R

object BindingAdapterMethods {
    @BindingAdapter(
        value = ["loadImage", "placeHolder", "errorHolder"],
        requireAll = false
    )
    @JvmStatic
    fun setImageFromResourceWithPlaceHolder(
        imageView: ImageView, url: String?,
        placeHolder: Int, errorHolder: Int,
    ) {
        val errorRes = if (errorHolder == 0) R.drawable.ic_image else errorHolder
        url?.let {
            val placeHolderRes = if (placeHolder == 0) R.drawable.ic_image else placeHolder
            val requestOptions = RequestOptions()
                .placeholder(placeHolderRes)
                .error(errorRes)
            buildGlideRequest(imageView, url, requestOptions)
        } ?: run { imageView.setImageResource(errorRes) }
    }

    @BindingAdapter("loadGalleryItem")
    @JvmStatic
    fun setGalleryCellSize(imageView: ImageView, pathUrl: String?) {

        pathUrl?.let {
            val context = imageView.context
            val screenWidth: Int = ScreenUtils.getDeviceWidth(context)
            val cellSize = screenWidth / context.resources.getInteger(R.integer.gallery_columns)
            imageView.layoutParams =
                (imageView.layoutParams as ConstraintLayout.LayoutParams).apply {
                    width = cellSize
                    height = cellSize
                }

            val requestOptions = RequestOptions()
                .placeholder(ColorDrawable(Color.WHITE))
                .error(ColorDrawable(Color.WHITE))
                .override(cellSize, cellSize)
            buildGlideRequest(imageView, pathUrl, requestOptions)
        } ?: run { imageView.setImageResource(R.color.white) }
    }

    private fun buildGlideRequest(
        imageView: ImageView,
        url: String,
        requestOptions: RequestOptions
    ) {
        Glide.with(imageView.context)
            .load(url)
            .thumbnail(0.05f)
            .apply(requestOptions)
            .into(imageView)
    }

    @BindingAdapter("drawableTop")
    @JvmStatic
    fun setDrawableTop(view: TextView, resourceId: Int) {
        val drawable: Drawable? = ContextCompat.getDrawable(view.context, resourceId)
        setIntrinsicBounds(drawable)
        val drawables: Array<Drawable> = view.compoundDrawables
        view.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3])
    }

    private fun setIntrinsicBounds(drawable: Drawable?) {
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    }
}