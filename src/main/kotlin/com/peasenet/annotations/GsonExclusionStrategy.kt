package com.peasenet.annotations

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

class GsonExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return clazz.isAnnotationPresent(Exclude::class.java)
    }

    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.getAnnotation(Exclude::class.java) != null
    }
}