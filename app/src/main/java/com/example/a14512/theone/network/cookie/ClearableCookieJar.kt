package com.example.a14512.weatherkotlin.network.cookie

import okhttp3.CookieJar

/**
 * @author 14512 on 2018/3/17
 */
interface ClearableCookieJar : CookieJar{
    /**
     * 清除所有会话cookie，同时保留持久的cookie。
     */
    abstract fun clearSession()

    /**
     * 清除所有cookie
     */
    abstract fun clear()
}