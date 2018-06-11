package com.example.a14512.weatherkotlin.network.cookie.persistence

import okhttp3.Cookie

/**
 * CookiePersistor处理持久化Cookie存储。
 * @author 14512 on 2018/3/17
 */
interface CookiePersistor {
    fun loadAll(): List<Cookie>

    /**
     * 持久化所有Cookie，现有Cookie将被覆盖。
     * @param cookies
     */
    fun saveAll(cookies: Collection<Cookie>)

    /**
     * 从删除指定持久化的Cookie
     * @param cookies
     */
    fun removeAll(cookies: Collection<Cookie>)

    /**
     * 清除所有持久化的Cookie
     */
    fun clear()
}