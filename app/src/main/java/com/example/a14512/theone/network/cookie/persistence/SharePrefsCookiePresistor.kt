package com.example.a14512.weatherkotlin.network.cookie.persistence

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Cookie

/**
 * @author 14512 on 2018/3/17
 */
class SharePrefsCookiePresistor(mContext: Context) : CookiePersistor{
    private var sharedPreferences: SharedPreferences =
            mContext.getSharedPreferences("CookiePersistence", Context.MODE_PRIVATE)


    override fun loadAll(): List<Cookie> {
        val cookies = ArrayList<Cookie>(sharedPreferences.all.size)
        for ((_, value) in sharedPreferences.all) {
            val serilaizedCookie = value as String
            val cookie = SerializableCookie().decode(serilaizedCookie)
            if (cookie != null) {
                cookies.add(cookie)
            }
        }
        return cookies
    }

    override fun saveAll(cookies: Collection<Cookie>) {
        val editor = sharedPreferences.edit()
        for (cookie in cookies) {
            editor.putString(createCookieKey(cookie), SerializableCookie().encode(cookie))
        }
        editor.apply()
    }

    override fun removeAll(cookies: Collection<Cookie>) {
        val editor = sharedPreferences.edit()
        for (cookie in cookies) {
            editor.remove(createCookieKey(cookie))
        }
        editor.apply()
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    private fun createCookieKey(cookie: Cookie): String {
        return (if (cookie.secure()) "https" else "http") + "://" + cookie.domain() + cookie.path() + "|" + cookie.name()
    }
}