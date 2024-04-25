package com.dd.android_initialize

import android.content.Context

interface Initialize<T> {

    /**
     * 初始化工作
     * */
    fun onCreate(context: Context): T?

    /**
     * 优先级
     * */
    fun priority(): Int = 0

    /**
     * 是否是主线程
     * */
    fun isMainThread() = true

    /**
     * 等待依赖项启动完成
     */
    fun toWait()

    /**
     * 在依赖项启动完成时通知启动。
     */
    fun toNotify()

    /**
     * 需要执行完才结束的任务
     * */
    fun needMainThreadWait() = isMainThread()
}