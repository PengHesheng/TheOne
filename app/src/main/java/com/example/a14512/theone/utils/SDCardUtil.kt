package com.example.a14512.theone.utils

/**
 * @author 14512 on 2018/5/30
 */
object SDCardUtil {

    fun checkSdCard(): Boolean {
        return (android.os.Environment.getExternalStorageState()
                == android.os.Environment.MEDIA_MOUNTED)
    }
}