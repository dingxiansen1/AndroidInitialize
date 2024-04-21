package com.dd.android_initialize

import android.content.Context

class TwoInitialize :AndroidInitialize<String>() {

    override fun onCreate(context: Context): String? {
        Thread.sleep(5000L)
        return ""
    }

    override fun isMainThread(): Boolean {
        return false
    }
}