package com.dd.android_initialize

class AndroidInitializeCache private constructor() {

    companion object {
        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AndroidInitializeCache()
        }
    }


    private val notifyInitialized: HashMap<Class<*>, List<AndroidInitialize<*>>> = HashMap()

    fun checkAndOrganize(list: List<AndroidInitialize<*>>) {
        list.forEach {
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
    }

    fun getDependencies(kClass: Class<*>): List<AndroidInitialize<*>>? {
        return notifyInitialized[kClass]
    }
}