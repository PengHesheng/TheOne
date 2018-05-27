package com.example.a14512.theone.model.i

import cn.bmob.newim.listener.BmobListener1
import cn.bmob.v3.exception.BmobException
import com.example.a14512.theone.model.User

/**
 * @author 14512 on 2018/5/2
 */
abstract class QueryUserListener: BmobListener1<User>() {
    abstract fun done(s: User?, e: BmobException?)

    override fun postDone(o: User?, e: BmobException?) {
        done(o, e)
    }
}