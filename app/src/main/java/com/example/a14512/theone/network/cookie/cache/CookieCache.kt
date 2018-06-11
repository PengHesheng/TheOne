package com.example.a14512.weatherkotlin.network.cookie.cache

import okhttp3.Cookie

/**
 * @author 14512 on 2018/3/17
 */
interface CookieCache: MutableIterable<Cookie> {

    /**
     * 将所有新的Cookie添加到会话中，现有的Cookie将被覆盖。
     * @param cookies
     */
    fun addAll(coolies: Collection<Cookie>)

    /**
     * 清除会话中的所有Cookie。
     */
    fun clear()
}