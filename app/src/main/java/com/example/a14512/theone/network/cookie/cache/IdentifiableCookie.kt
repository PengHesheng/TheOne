package com.example.a14512.weatherkotlin.network.cookie.cache

import okhttp3.Cookie

/**
 * @author 14512 on 2018/3/17
 */
class IdentifiableCookie(private var cookie: Cookie) {

    companion object {
        fun decorateAll(cookies: Collection<Cookie>): List<IdentifiableCookie> {
            var identifiableCookies = ArrayList<IdentifiableCookie>(cookies.size)
            for (cookie in cookies) {
                identifiableCookies.add(IdentifiableCookie(cookie))
            }
            return identifiableCookies
        }
    }

    fun getCookie(): Cookie {
        return cookie
    }

    override fun hashCode(): Int {
        var hash = 17
        hash = 31 * hash + cookie.name().hashCode()
        hash = 31 * hash + cookie.domain().hashCode()
        hash = 31 * hash + cookie.path().hashCode()
        hash = 31 * hash + if (cookie.secure()) 0 else 1
        hash = 31 * hash + if (cookie.hostOnly()) 0 else 1
        return hash
    }

    override fun equals(other: Any?): Boolean {
        if (other !is IdentifiableCookie)
            return false
        val that: IdentifiableCookie = other

        return (that.cookie.name() == this.cookie.name()
                && that.cookie.domain() == this.cookie.domain()
                && that.cookie.path() == that.cookie.path()
                && that.cookie.secure() == this.cookie.secure()
                && that.cookie.hostOnly() == this.cookie.hostOnly())
    }
}