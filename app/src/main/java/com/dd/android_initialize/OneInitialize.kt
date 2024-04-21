package com.dd.android_initialize

import android.content.Context

class OneInitialize :AndroidInitialize<String>() {

    override fun onCreate(context: Context): String? {
        Thread.sleep(2000L)
        return ""
    }

    override fun isMainThread(): Boolean {
        return true
    }

    override fun dependencies(): List<Class<out AndroidInitialize<*>>> {
        return arrayListOf(TwoInitialize::class.java,TreeInitialize::class.java)
    }
}