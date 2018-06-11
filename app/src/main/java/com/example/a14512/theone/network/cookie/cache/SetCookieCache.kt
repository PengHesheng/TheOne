package com.example.a14512.weatherkotlin.network.cookie.cache

import okhttp3.Cookie
import java.util.*

/**
 * @author 14512 on 2018/3/17
 */
class SetCookieCache : CookieCache{
    var cookies: MutableSet<IdentifiableCookie>

    init {
        this.cookies = HashSet()
    }

    override fun clear() {
        cookies.clear()
    }

    override fun iterator(): MutableIterator<Cookie> {
        return SetCookieCacheIterator()
    }

    override fun addAll(coolies: Collection<Cookie>) {
        for (cookie in IdentifiableCookie.decorateAll(coolies)) {
            this.cookies.remove(cookie)
            this.cookies.add(cookie)
        }
    }

    private inner class SetCookieCacheIterator : MutableIterator<Cookie> {
        private var iterator: MutableIterator<IdentifiableCookie> = cookies.iterator()

        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }

        override fun next(): Cookie {
            return iterator.next().getCookie()
        }

        override fun remove() {
            iterator.remove()
        }

    }
}