package com.example.a14512.theone.network.rx_util.interceptor

import com.example.a14512.theone.network.Result
import com.example.a14512.weatherkotlin.network.RxUtil.exception.ServiceException
import io.reactivex.functions.Function

/**
 * @author 14512 on 2018/3/17
 */
class ServiceResponseFun<T> : Function<Result<T>, T> {

    @Throws(Exception::class)
    override fun apply(tResult: Result<T>): T {
        if (tResult.status != 200) {
            throw ServiceException(tResult.status, tResult.info)
        }
        return tResult.data
    }

}