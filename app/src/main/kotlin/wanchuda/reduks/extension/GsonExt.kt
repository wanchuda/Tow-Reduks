package wanchuda.reduks.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import wanchuda.reduks.common.AppGson
import java.io.Reader
import java.lang.reflect.Type

fun <T> T.toJson(): String = Gson().toJson(this)
inline fun <reified T> String.toObject() = AppGson().fromJson<T>(this, typeToken<T>())
inline fun <reified T> Reader.toObject() = AppGson().fromJson<T>(this, typeToken<T>())
inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, typeToken<T>())
inline fun <reified T> Gson.fromJson(json: Reader) = this.fromJson<T>(json, typeToken<T>())
inline fun <reified T> typeToken(): Type = object : TypeToken<T>() {}.type