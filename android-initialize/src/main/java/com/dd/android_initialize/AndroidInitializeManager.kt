package com.dd.android_initialize


import android.content.Context
import androidx.tracing.Trace
import com.dd.android_initialize.executor.ExecutorManager
import com.dd.android_initialize.runnable.TaskRunnable
import com.dd.android_initialize.utils.Logger
import com.dd.android_initialize.utils.SortUtils

class AndroidInitializeManager private constructor(private val context: Context, builder: Builder) {

    private var mStartupList = mutableListOf<AndroidInitialize<*>>()
    private val initialized: HashMap<Class<*>, Any> = HashMap()

    init {
        Logger.instance.init(builder.mTag)
        mStartupList = builder.mStartupList
    }

    private val TAG = "AndroidInitializeManager"

    private val mLock = Any()

    fun start() {
        val startTime = System.currentTimeMillis()
        Trace.beginSection(AndroidInitializeManager::class.java.simpleName)
        mStartupList = SortUtils.sort(mStartupList).toMutableList()
        AndroidInitializeCache.instance.checkAndOrganize(mStartupList)
        mStartupList.forEach {
            initialize(it)
        }
        AndroidInitializeCache.instance.mainThreadWait()
        Trace.endSection()
        val endTime = System.currentTimeMillis()
        Logger.instance.d(
            AndroidInitializeManager::class.java.simpleName,
            "开始时间:$startTime" + ",结束时间:$endTime" + ",总耗时:${endTime - startTime}ms"
        )
    }

    private fun initialize(initialize: AndroidInitialize<*>) {
        val dependencies = initialize.dependencies()
        if (initialize.dependencies().isNotEmpty()) {
            dependencies.forEach { depend ->
                if (!initialized.containsKey(depend)) {
                    obtainInitialize(depend).let {
                        initialize(it)
                    }
                }
            }
        }
        synchronized(mLock) {
            if (initialized.containsKey(initialize::class.java)) {
                return
            }
            initialized[initialize::class.java] = initialize
        }
        Logger.instance.d(TAG, "initialize:" + initialize::class.java.simpleName)
        val runnable = TaskRunnable(context, initialize)
        if (initialize.isMainThread()) {
            runnable.run()
        } else {
            ExecutorManager.instance.ioExecutor.execute(runnable)
        }
    }

    private fun <T : AndroidInitialize<*>> obtainInitialize(component: Class<out T>): AndroidInitialize<*> {
        mStartupList.forEach {
            if (it::class == component) {
                return it
            }
        }
        val instance: Any = component.getDeclaredConstructor().newInstance()
        val initializer: AndroidInitialize<*> = instance as AndroidInitialize<*>
        mStartupList.add(0, initializer)
        return initializer
    }

    class Builder {
        internal var mStartupList = mutableListOf<AndroidInitialize<*>>()
        internal var mWaitSize = 0
        internal var mTag: String = ""

        fun addStartup(startup: AndroidInitialize<*>) = apply {
            mStartupList.add(startup)
        }

        fun addAllStartup(list: List<AndroidInitialize<*>>) = apply {
            list.forEach {
                addStartup(it)
            }
        }

        fun setTag(tag: String) = apply {
            mTag = tag
        }


        fun build(context: Context): AndroidInitializeManager {

            return AndroidInitializeManager(context, this)
        }
    }


}