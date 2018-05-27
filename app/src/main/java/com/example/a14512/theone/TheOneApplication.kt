package com.example.a14512.theone

import android.app.Application
import android.content.Context
import cn.bmob.newim.BmobIM
import com.example.a14512.theone.net.TheOneMessageHandler
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.properties.Delegates

/**
 * @author 14512 on 2018/5/1
 */
class TheOneApplication : Application() {

    companion object {
        private var mContext : TheOneApplication by Delegates.notNull()
        var mCacheDir : String by Delegates.notNull()

        fun getContext(): Context = mContext
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        //集成：1.8、初始化IM SDK，并注册消息接收器
        if (applicationInfo.packageName == getMyProcessName()) {
            BmobIM.init(this)
            BmobIM.registerDefaultMessageHandler(TheOneMessageHandler(mContext))
        }

        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        mCacheDir = if (applicationContext.externalCacheDir != null) {
            applicationContext.externalCacheDir.toString()
        } else {
            applicationContext.cacheDir.toString()
        }
    }

    private fun getMyProcessName(): String? {
        return try {
            val file = File("/proc/" + android.os.Process.myPid() + "/" + "cmdline")
            val mBufferedReader = BufferedReader(FileReader(file))
            val processName = mBufferedReader.readLine().trim { it <= ' ' }
            mBufferedReader.close()
            processName
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
}