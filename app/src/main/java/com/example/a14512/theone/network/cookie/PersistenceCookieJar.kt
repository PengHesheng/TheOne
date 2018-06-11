package com.example.a14512.weatherkotlin.network.cookie

import com.example.a14512.weatherkotlin.network.cookie.cache.CookieCache
import com.example.a14512.weatherkotlin.network.cookie.persistence.CookiePersistor
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.*

/**
 * @author 14512 on 2018/3/17
 */
class PersistenceCookieJar(private var cache: CookieCache,
                           private var persistor: CookiePersistor) : ClearableCookieJar {

    init {
        this.cache.addAll(persistor.loadAll())
    }

    override fun clearSession() {
        cache.clear()
        cache.addAll(persistor.loadAll())
    }

    override fun clear() {
        cache.clear()
        persistor.clear()
    }

    override fun saveFromResponse(url: HttpUrl?, cookies: List<Cookie>?) {
        if (cookies != null) {
            cache.addAll(cookies)
            persistor.saveAll(filterPersistenceCookie(cookies))
        }
    }

    override fun loadForRequest(url: HttpUrl?): List<Cookie>? {
        val cookiesToRemove = ArrayList<Cookie>()
        val validCookies = ArrayList<Cookie>()
        val iterator = cache.iterator()
        while (iterator.hasNext()) {
            val currentCookie = iterator.next()
            if (isCookieWxpired(currentCookie)) {
                cookiesToRemove.add(currentCookie)
                iterator.remove()
            } else if (currentCookie.matches(url)) {
                validCookies.add(currentCookie)
            }
        }
        persistor.removeAll(cookiesToRemove)
        return validCookies
    }

    private fun isCookieWxpired(cookie: Cookie): Boolean {
        return cookie.expiresAt() < System.currentTimeMillis()
    }

    private fun filterPersistenceCookie(cookies: List<Cookie>): List<Cookie> {
        val persistenceCookies = ArrayList<Cookie>()
        for (cookie in cookies) {
            if (cookie.persistent()) {
                persistenceCookies.add(cookie)
            }
        }
        return persistenceCookies
    }

}