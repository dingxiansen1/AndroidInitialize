package com.dd.android_initialize.utils

import androidx.tracing.Trace
import com.dd.android_initialize.AndroidInitialize

object SortUtils {

    fun sort(list: List<AndroidInitialize<*>>) : List<AndroidInitialize<*>>{
        Trace.beginSection(SortUtils::class.java.simpleName)
        val newList = list.sortedByDescending {
            it.priority()
        }
        Trace.endSection()
        return newList
    }

}