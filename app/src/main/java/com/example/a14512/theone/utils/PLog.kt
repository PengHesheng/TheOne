package com.example.a14512.theone.utils

import android.util.Log
import com.example.a14512.theone.BuildConfig
import com.example.a14512.theone.LOG_DATE
import com.example.a14512.theone.TheOneApplication.Companion.mCacheDir
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * @author 14512 on 2018/3/17
 */
object PLog {
    val PATH = "$mCacheDir/Log"

    val PLOG_FILE_NAME = "log.txt"

    // 是否写入日志文件
    val PLOG_WRITE_TO_FILE = true

    val PLOG_PRINT_LOG = true

    var isDebug = BuildConfig.DEBUG

    /**
     * 错误信息
     */
    fun e(TAG: String, msg: String) {
        if (PLOG_PRINT_LOG) {
            Log.e(TAG, log(msg))
        }

        if (PLOG_WRITE_TO_FILE) {
            writeLogtoFile("e", TAG, msg)
        }
    }

    /**
     * 警告信息
     */
    fun w(TAG: String, msg: String) {
        if (PLOG_PRINT_LOG) {
            Log.w(TAG, log(msg))
        }
        if (PLOG_WRITE_TO_FILE) {
            writeLogtoFile("w", TAG, msg)
        }
    }

    /**
     * 调试信息
     */
    fun d(TAG: String, msg: String) {
        if (PLOG_PRINT_LOG) {
            Log.d(TAG, log(msg))
        }
        if (PLOG_WRITE_TO_FILE) {
            writeLogtoFile("d", TAG, msg)
        }
    }

    /**
     * 提示信息
     */
    fun i(TAG: String, msg: String) {
        if (PLOG_PRINT_LOG) {
            Log.i(TAG, log(msg))
        }
        if (PLOG_WRITE_TO_FILE) {
            writeLogtoFile("i", TAG, msg)
        }
    }

    fun d(msg: String) {
        if (isDebug) {
            d(getClassName(), msg)
        }
    }

    fun e(msg: String) {
        e(getClassName(), msg) // 错误信息都得打印出来才行
    }

    fun i(msg: String) {
        if (isDebug) {
            i(getClassName(), msg)
        }
    }

    fun w(msg: String) {
        if (isDebug) {
            w(getClassName(), msg)
        }
    }

    /**
     * @return 当前的类名(simpleName)
     */
    fun getClassName(): String {
        var result: String
        val thisMethodStack = Thread.currentThread().stackTrace[2]
        result = thisMethodStack.className
        val lastIndex = result.lastIndexOf(".")
        result = result.substring(lastIndex + 1)

        // TODO: 16/5/10 调试下 内部类问题
        val i = result.indexOf("$")// 剔除匿名内部类名
        return if (i == -1) result else result.substring(0, i)
    }


    /**
     * 打印 Log 行数位置
     */
    fun log(message: String): String {
        val stackTrace = Thread.currentThread().stackTrace
        val targetElement = stackTrace[5]
        var className = targetElement.className
        val i = className.indexOf("$")// 剔除匿名内部类名
        if (i != -1) {
            className = className.substring(0, i)
        }
        className = className.substring(className.lastIndexOf('.') + 1) + ".java"
        var lineNumber = targetElement.lineNumber
        if (lineNumber < 0) {
            lineNumber = 0
        }
        return "($className:$lineNumber) $message"
    }


    /**
     * 写入日志到文件中
     */
    private fun writeLogtoFile(mylogtype: String, tag: String, msg: String) {
        isExist(PATH)
        isDel()
        val needWriteMessage = ("\r\n"
                + Time.getNowMDHMSTime()
                + "\r\n"
                + mylogtype
                + "    "
                + tag
                + "\r\n"
                + msg)
        val file = File(PATH, PLOG_FILE_NAME)
        try {
            val filerWriter = FileWriter(file, true)
            val bufWriter = BufferedWriter(filerWriter)
            bufWriter.write(needWriteMessage)
            bufWriter.newLine()
            bufWriter.close()
            filerWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun isDel() {
        val date = Time.getNowYMD()
        if (!ACache.getDefault().getAsString(LOG_DATE).equals(date)) {
            delFile()
        }
        ACache.getDefault().put(LOG_DATE, date)
    }

    /**
     * 删除日志文件
     */
    fun delFile() {

        val file = File(PATH, PLOG_FILE_NAME)
        if (file.exists()) {
            file.delete()
        }
    }

    /**
     * 判断文件夹是否存在,如果不存在则创建文件夹
     */
    fun isExist(path: String) {
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}