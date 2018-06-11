package com.example.a14512.theone.network

import com.example.a14512.theone.model.DateGank
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author 14512 on 2018/3/17
 */
interface ApiService {

    @GET("day/{date}")
    fun getGankDate(@Path("date") date: String): Observable<DateGank>

}