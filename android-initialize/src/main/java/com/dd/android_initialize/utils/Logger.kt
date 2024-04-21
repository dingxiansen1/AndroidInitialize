package com.dd.android_initialize.utils

import android.util.Log

internal class Logger private constructor() {

    companion object {
        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Logger()
        }
        private var TAG = ""
        private const val DIVIDE = "--->"
    }

    internal fun init(tag: String) {
        TAG = tag
    }

    fun d(tag: String, msg: String) {
        Log.d(TAG, tag + DIVIDE + msg)
    }

    fun w(tag: String, msg: String) {
        Log.w(TAG, tag + DIVIDE + msg)
    }

    fun e(tag: String, msg: String) {
        Log.e(TAG, tag + DIVIDE + msg)
    }
}