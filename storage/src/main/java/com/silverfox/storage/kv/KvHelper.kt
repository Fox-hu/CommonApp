package com.silverfox.storage.kv

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV
import java.lang.reflect.ParameterizedType

/**
 * @Author fox.hu
 * @Date 2019/12/26 15:29
 */
class KvHelper {
    companion object {

        @JvmStatic
        fun init(context: Context){
            MMKV.initialize(context)
        }

        private val kv: MMKV by lazy {
            MMKV.defaultMMKV()
        }

        @JvmStatic
        fun <T> put(key: String, value: T) {
            when (value) {
                is Long -> kv.encode(key, value)
                is String -> kv.encode(key, value)
                is Int -> kv.encode(key, value)
                is Boolean -> kv.encode(key, value)
                is Float -> kv.encode(key, value)
                is Parcelable -> kv.encode(key, value)
                is Double -> kv.encode(key, value)
                is ByteArray -> kv.encode(key, value)
                else -> kv.encode(key, value.toJson())
            }
        }

        @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
        @JvmStatic
        fun <T> getOrDefault(key: String, default: T): T {
            val res = when (default) {
                is Long -> kv.decodeLong(key, default)
                is String -> kv.decodeString(key, default)
                is Int -> kv.decodeInt(key, default)
                is Boolean -> kv.decodeBool(key, default)
                is Float -> kv.decodeFloat(key, default)
                is Double -> kv.decodeDouble(key, default)
                is ByteArray -> kv.decodeBytes(key, default)
                is Parcelable -> {
                    val clazz = (default.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<Parcelable>
                    kv.decodeParcelable(key, clazz, default)
                }
                else -> kv.decodeString(key, "").toEntity<T>() ?: default
            }
            return res as T
        }
    }
}