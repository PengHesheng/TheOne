package com.example.a14512.weatherkotlin.network.RxUtil.exception

import java.lang.Exception

/**
 * @author 14512 on 2018/3/17
 */
class ApiException(override var cause: Throwable, private var code: Int) : Exception() {
    private var displayMessage: String = ""

    fun getDisplayMessage(): String {
        return displayMessage
    }

    fun setDisplayMessage(displayMessage: String) {
        this.displayMessage = displayMessage
    }

    fun getCode(): Int {
        return code
    }
}