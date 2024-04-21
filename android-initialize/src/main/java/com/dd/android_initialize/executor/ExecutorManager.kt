package com.dd.android_initialize.executor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.max
import kotlin.math.min

class ExecutorManager private constructor(){


    companion object {

        @JvmStatic
        val instance by lazy { ExecutorManager() }
        //CPU
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        //核心线程池数量
        private val CORE_POOL_SIZE = max(2, min(CPU_COUNT - 1, 5))
    }
    var cpuExecutor: ExecutorService
        private set

    var ioExecutor: ExecutorService
        private set

    var mainExecutor: Executor
        private set

    init {
        cpuExecutor = Executors.newFixedThreadPool(CORE_POOL_SIZE)

        ioExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory())

        mainExecutor = object : Executor {
            private val handler = Handler(Looper.getMainLooper())

            override fun execute(command: Runnable) {
                handler.post(command)
            }
        }
    }
}