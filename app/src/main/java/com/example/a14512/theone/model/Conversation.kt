package com.example.a14512.theone.model

import android.content.Context
import cn.bmob.newim.bean.BmobIMConversationType
import java.io.Serializable

/**
 * @author 14512 on 2018/5/3
 */
abstract class Conversation : Serializable, Comparable<Any> {
    /**
     * 会话id
     */
    protected lateinit var cId: String

    /**
     * 会话类型
     */
    protected lateinit var cType: BmobIMConversationType

    /**
     * 会话名称
     */
    protected lateinit var cName: String

    /**
     * 获取头像-用于会话界面显示
     */
    abstract fun getAvatar(): Any

    /**
     * 获取最后一条消息的时间
     */
    abstract fun getLastMessageTime(): Long

    /**
     * 获取最后一条消息的内容
     * @return
     */
    abstract fun getLastMessageContent(): String

    /**
     * 获取未读会话个数
     * @return
     */
    abstract fun getUnReadCount(): Int

    /**
     * 将所有消息标记为已读
     */
    abstract fun readAllMessage()

    /**
     * 点击事件
     * @param context
     */
    abstract fun onClick(context: Context)

    /**
     * 长按事件
     * @param context
     */
    abstract fun onLongClick(context: Context)

    fun getcName(): String {
        return cName
    }

    fun getcId(): String {
        return cId
    }

    fun getcType(): BmobIMConversationType {
        return cType
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as Conversation?
        return if (cId != that!!.cId) false else cType == that.cType
    }

    override fun hashCode(): Int {
        var result = cId.hashCode()
        result = 31 * result + cType.hashCode()
        return result
    }

    override fun compareTo(other: Any): Int {
        if (other is Conversation) {
            var otherConversation = other
            var timeGap = otherConversation.getLastMessageTime() - getLastMessageTime()
            if (timeGap > 0) {
                return 1
            } else if (timeGap < 0) {
                return -1
            }
            return 0
        } else {
            throw ClassCastException()
        }
    }
}