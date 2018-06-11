package com.example.a14512.theone.network

import com.example.a14512.weatherkotlin.network.RxUtil.ApiObserver
import com.google.gson.annotations.SerializedName

/**
 * @author 14512 on 2018/3/17
 */
data class Result<out T>(@SerializedName("status") val status: Int,
                         @SerializedName("data") val data: T,
                         @SerializedName("info") val info: String) {

    //示例网络请求
    var apiObserver = object : ApiObserver<String>() {
        override fun onNext(t: String) {

        }
    }

}