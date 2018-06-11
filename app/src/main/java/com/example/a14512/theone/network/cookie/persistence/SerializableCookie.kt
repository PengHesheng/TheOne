package com.example.a14512.weatherkotlin.network.cookie.persistence

import android.util.Log
import okhttp3.Cookie
import java.io.*
import kotlin.experimental.and

/**
 * @author 14512 on 2018/3/17
 */
class SerializableCookie : Serializable {
    private val TAG = SerializableCookie::class.java.simpleName

    private val serialVersionUID = -4379853716078535580L
    //不需要序列化
    @Transient
    private var cookie: Cookie? = null

    fun encode(cookie: Cookie): String? {
        this.cookie = cookie

        val byteArrayOutputStream = ByteArrayOutputStream()
        var objectOutputStream: ObjectOutputStream? = null

        try {
            objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
            objectOutputStream.writeObject(this)
        } catch (e: IOException) {
            Log.d(TAG, "IOException in encodeCookie", e)
            return null
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close()
                } catch (e: IOException) {
                    Log.d(TAG, "Stram not closed in encodeCookie", e)
                }

            }
        }
        return byteArrayToHexString(byteArrayOutputStream.toByteArray())
    }

    private fun byteArrayToHexString(bytes: ByteArray): String {
        val stringBuilder = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v = element and 0xff.toByte()
            if (v < 16) {
                stringBuilder.append('0')
            }
            stringBuilder.append(Integer.toHexString(v.toInt()))
        }
        return stringBuilder.toString()
    }

    fun decode(encodeCookie: String): Cookie? {
        val bytes = hexStringToByteArray(encodeCookie)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: Cookie? = null
        var objectInputStream: ObjectInputStream? = null
        try {
            objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as SerializableCookie).cookie
        } catch (e: IOException) {
            Log.d(TAG, "IOException in decodeCookie", e)
        } catch (e: ClassNotFoundException) {
            Log.d(TAG, "ClassNotFoundException in decodeCookie", e)
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close()
                } catch (e: IOException) {
                    Log.d(TAG, "Stream not closed in decodeCookie", e)
                }

            }
        }
        return cookie

    }

    /**
     * Converts hex values from strings to byte array
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(hexString[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    private val NON_VALID_EXPIRES_AT = -1L

    @Throws(IOException::class)
    private fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeObject(cookie!!.name())
        outputStream.writeObject(cookie!!.value())
        outputStream.writeObject(if (cookie!!.persistent()) cookie!!.expiresAt() else NON_VALID_EXPIRES_AT)
        outputStream.writeObject(cookie!!.domain())
        outputStream.writeObject(cookie!!.path())
        outputStream.writeObject(cookie!!.secure())
        outputStream.writeObject(cookie!!.httpOnly())
        outputStream.writeObject(cookie!!.hostOnly())
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inputStream: ObjectInputStream) {
        val builder = Cookie.Builder()
        builder.name(inputStream.readObject() as String)
        builder.value(inputStream.readObject() as String)

        val expiresAt = inputStream.readLong()
        if (expiresAt != NON_VALID_EXPIRES_AT) {
            builder.expiresAt(expiresAt)
        }

        val domain = inputStream.readObject() as String
        builder.domain(domain)

        builder.path(inputStream.readObject() as String)

        if (inputStream.readBoolean()) {
            builder.secure()
        }
        if (inputStream.readBoolean()) {
            builder.httpOnly()
        }
        if (inputStream.readBoolean()) {
            builder.hostOnlyDomain(domain)
        }

        cookie = builder.build()
    }
}