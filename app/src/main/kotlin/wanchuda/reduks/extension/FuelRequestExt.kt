package wanchuda.reduks.extension

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.rx.rx_object
import java.io.Reader
import java.nio.charset.Charset

fun Request.jsonBody(body: String, charset: Charset = Charsets.UTF_8) = header("Content-Type" to "application/json").body(body, charset)

fun Request.jsonBody(body: ByteArray) = header("Content-Type" to "application/json").body(body)

inline fun <reified T : Any> Request.rx_gson() = rx_object(object : ResponseDeserializable<T> {
    override fun deserialize(reader: Reader): T {
        return reader.toObject<T>()
    }
})

inline fun <reified T : Any> Request.rx_gsonResult() = rx_gson<T>().mapResult()