package com.usman.gallerydemo.utils

import androidx.databinding.library.BuildConfig
import timber.log.Timber

object AppLogger {

    fun tag(s: String?): Timber.Tree {
        return Timber.tag(s)
    }

    fun d(tag: String?, s: String?) {
        Timber.tag(tag).d(s)
    }

    fun e(tag: String?, s: String?) {
        Timber.tag(tag).e(s)
    }

    fun d(tag: String?, s: String?, vararg objects: Any?) {
        Timber.tag(tag).d(s, objects)
    }

    fun d(s: String?, vararg objects: Any?) {
        Timber.d(s, objects)
    }

    fun d(throwable: Throwable?, s: String?, vararg objects: Any?) {
        Timber.d(throwable, s, objects)
    }

    fun e(s: String?, vararg objects: Any?) {
        Timber.e(s, objects)
    }

    fun e(throwable: Throwable?, s: String?, vararg objects: Any?) {
        Timber.e(throwable, s, objects)
    }

    fun i(s: String?, vararg objects: Any?) {
        Timber.i(s, objects)
    }

    fun i(throwable: Throwable?, s: String?, vararg objects: Any?) {
        Timber.i(throwable, s, objects)
    }

    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun w(s: String?, vararg objects: Any?) {
        Timber.w(s, objects)
    }

    fun w(throwable: Throwable?, s: String?, vararg objects: Any?) {
        Timber.w(throwable, s, objects)
    }
}