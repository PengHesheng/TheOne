package com.example.a14512.weatherkotlin.network.RxUtil.interceptor

import com.example.a14512.weatherkotlin.network.RxUtil.exception.ExceptionHandle
import io.reactivex.Observable
import io.reactivex.functions.Function

/**
 * @author 14512 on 2018/3/17
 */
class HttpResponseFun<T> : Function<Throwable, Observable<T>> {
    override fun apply(t: Throwable): Observable<T> {
        return Observable.error(ExceptionHandle.handleException(t))
    }
}