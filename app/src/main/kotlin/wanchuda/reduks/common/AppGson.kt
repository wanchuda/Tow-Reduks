package wanchuda.reduks.common

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import io.realm.RealmObject

fun AppGson() = GsonBuilder()
        .setExclusionStrategies(RealmObjectGsonExclusion())
        .create()

class RealmObjectGsonExclusion : ExclusionStrategy {

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }

    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return f?.declaringClass?.equals(RealmObject::class.java)!!
    }

}