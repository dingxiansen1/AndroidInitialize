package com.dd.android_initialize

import java.util.concurrent.CountDownLatch

abstract class AndroidInitialize<T> : Initialize<T> {
    /**
     * 需要等待的数量
     * */
    private val mWaitCountDown by lazy { CountDownLatch(dependencies().size) }

    override fun toWait() {
        mWaitCountDown.await()
    }

    override fun toNotify() {
        mWaitCountDown.countDown()
    }


    /**
     * 前置依赖任务列表
     * */
   open fun dependencies(): List<Class<out AndroidInitialize<*>>> = mutableListOf()
}