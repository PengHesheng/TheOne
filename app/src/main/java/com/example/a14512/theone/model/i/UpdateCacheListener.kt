package com.example.a14512.theone.model.i

import cn.bmob.newim.listener.BmobListener1
import cn.bmob.v3.exception.BmobException

/**
 * @author 14512 on 2018/5/2
 */
abstract class UpdateCacheListener: BmobListener1<Object>() {

    abstract fun done(e: BmobException?)

    override fun postDone(o: Object?, e: BmobException?) {
        done(e!!)
    }
}