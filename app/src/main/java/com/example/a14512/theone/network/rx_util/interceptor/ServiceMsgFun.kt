package com.example.a14512.theone.network.rx_util.interceptor

import com.example.a14512.theone.network.Result
import io.reactivex.functions.Function

/**
 * @author 14512 on 2018/3/17
 */
class ServiceMsgFun<T> : Function<Result<T>, String> {

    @Throws(Exception::class)
    override fun apply(tResult: Result<T>): String {
        return tResult.info
    }
}