package com.dd.android_initialize

import android.content.Context

class TreeInitialize :AndroidInitialize<String>() {

    override fun onCreate(context: Context): String? {
        Thread.sleep(3000L)
        return ""
    }

    override fun isMainThread(): Boolean {
        return false
    }
}