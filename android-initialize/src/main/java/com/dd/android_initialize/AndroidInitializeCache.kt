package com.dd.android_initialize

import java.util.concurrent.CountDownLatch

class AndroidInitializeCache private constructor() {

    companion object {
        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AndroidInitializeCache()
        }
    }


    private val notifyInitialized: HashMap<Class<*>, List<AndroidInitialize<*>>> = HashMap()

    private lateinit var  mWaitCountDown :CountDownLatch

    fun checkAndOrganize(list: List<AndroidInitialize<*>>) {
        var size = 0
        list.forEach {
            if (it.needMainThreadWait()){
                size++
            }
            if (it.dependencies().isEmpty()) {
                return@forEach
            }
            //遍历依赖项
            it.dependencies().forEach { kClass ->
                //把自己加入该依赖项中
                val notifyList = notifyInitialized[kClass]?.toMutableList() ?: mutableListOf()
                notifyList.add(it)
                notifyInitialized[kClass] = notifyList
            }
        }
        mWaitCountDown = CountDownLatch(size)
    }

    fun getDependencies(kClass: Class<*>): List<AndroidInitialize<*>>? {
        return notifyInitialized[kClass]
    }

    fun mainThreadWait(){
        mWaitCountDown.await()
    }

    fun notify(){
        mWaitCountDown.countDown()
    }
}