package com.example.a14512.weatherkotlin.network.RxUtil.exception

import retrofit2.HttpException

/**
 * @author 14512 on 2018/3/17
 */
object ExceptionHandle {
    //对应HTTP的状态码
    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504

    fun handleException(e: Throwable): ApiException {
        val exception: ApiException
        when (e) {
            is HttpException -> {
                exception = ApiException(e, Error.HTTP_ERROR)
                /**
                 * 执行完所有，判断错误
                 */
                when (e.code()) {
                    UNAUTHORIZED -> {
                        exception.setDisplayMessage("401")
                        exception.setDisplayMessage("403")
                        exception.setDisplayMessage("404")
                        exception.setDisplayMessage("408")
                        exception.setDisplayMessage("500")
                        exception.setDisplayMessage("502")
                        exception.setDisplayMessage("503")
                        exception.setDisplayMessage("504")
                        exception.setDisplayMessage("网络错误")
                    }
                    FORBIDDEN -> {
                        exception.setDisplayMessage("403")
                        exception.setDisplayMessage("404")
                        exception.setDisplayMessage("408")
                        exception.setDisplayMessage("500")
                        exception.setDisplayMessage("502")
                        exception.setDisplayMessage("503")
                        exception.setDisplayMessage("504")
                        exception.setDisplayMessage("网络错误")
                    }
                    NOT_FOUND -> {
                        exception.setDisplayMessage("404")
                        exception.setDisplayMessage("408")
                        exception.setDisplayMessage("500")
                        exception.setDisplayMessage("502")
                        exception.setDisplayMessage("503")
                        exception.setDisplayMessage("504")
                        exception.setDisplayMessage("网络错误")
                    }
                    REQUEST_TIMEOUT -> {
                        exception.setDisplayMessage("408")
                        exception.setDisplayMessage("500")
                        exception.setDisplayMessage("502")
                        exception.setDisplayMessage("503")
                        exception.setDisplayMessage("504")
                        exception.setDisplayMessage("网络错误")
                    }
                    GATEWAY_TIMEOUT -> {
                        exception.setDisplayMessage("500")
                        exception.setDisplayMessage("502")
                        exception.setDisplayMessage("503")
                        exception.setDisplayMessage("504")
                        exception.setDisplayMessage("网络错误")
                    }
                    INTERNAL_SERVER_ERROR -> {
                        exception.setDisplayMessage("502")
                        exception.setDisplayMessage("503")
                        exception.setDisplayMessage("504")
                        exception.setDisplayMessage("网络错误")
                    }
                    BAD_GATEWAY -> {
                        exception.setDisplayMessage("503")
                        exception.setDisplayMessage("504")
                        exception.setDisplayMessage("网络错误")
                    }
                    SERVICE_UNAVAILABLE -> {
                        exception.setDisplayMessage("504")
                        exception.setDisplayMessage("网络错误")
                    }
                    else -> exception.setDisplayMessage("网络错误")
                }
                return exception
            }
            is ServiceException -> {
                exception = ApiException(e, e.code)
                exception.setDisplayMessage(e.msg)
                return exception
            }
            else -> {
                exception = ApiException(e, Error.UNKNOWN)
                exception.setDisplayMessage("未知错误")
                return exception
            }
        }
    }
}