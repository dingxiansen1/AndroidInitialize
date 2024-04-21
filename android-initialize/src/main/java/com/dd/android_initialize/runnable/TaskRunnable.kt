package com.dd.android_initialize.runnable

import android.content.Context
import androidx.tracing.Trace
import com.dd.android_initialize.AndroidInitialize
import com.dd.android_initialize.AndroidInitializeCache
import com.dd.android_initialize.utils.Logger

internal class TaskRunnable(
    private val context: Context,
    private val initialize: AndroidInitialize<*>,
) : Runnable {

    override fun run() {
        //等待依赖任务执行完毕
        initialize.toWait()
        Trace.beginSection(initialize::class.java.simpleName)
        val startTime = System.currentTimeMillis()
        val result = initialize.onCreate(context)
        val endTime = System.currentTimeMillis()
        Trace.endSection()
        Logger.instance.d(
            initialize::class.java.simpleName,
            "开始时间:$startTime" + ",结束时间:$endTime" + ",总耗时:${endTime - startTime}ms"
        )
        AndroidInitializeCache.instance.getDependencies(initialize::class.java)?.let {
            it.forEach {
                it.toNotify()
            }
        }
    }

}