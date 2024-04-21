package com.dd.android_initialize

import android.app.Application

class App :Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidInitializeManager.Builder()
            .setTag("com.dd.android_initialize")
            .addStartup(OneInitialize()).build(this).start()
    }

}