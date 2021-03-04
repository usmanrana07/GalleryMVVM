package com.usman.gallerydemo.utils

import android.content.Context
import android.content.res.Resources
import kotlin.math.roundToInt

object ScreenUtils {

    fun pxToDp(px: Float): Float {
        val densityDpi =
            Resources.getSystem().displayMetrics.densityDpi.toFloat()
        return px / (densityDpi / 160f)
    }

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).roundToInt()
    }

    fun getDeviceWidth(context: Context?): Int {
        return context?.resources?.displayMetrics?.widthPixels ?: 0
    }

    fun getDeviceHeight(context: Context?): Int {
        return context?.resources?.displayMetrics?.heightPixels ?: 0
    }

}