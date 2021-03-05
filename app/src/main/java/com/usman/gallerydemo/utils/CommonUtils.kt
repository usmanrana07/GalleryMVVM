package com.usman.gallerydemo.utils

import android.util.SparseArray

object CommonUtils {

    fun <C> asList(sparseArray: SparseArray<C>): MutableList<C> {
        val arrayList = arrayListOf<C>()
        for (i in 0 until sparseArray.size()) arrayList.add(sparseArray.valueAt(i))
        return arrayList
    }
}