package com.example.a14512.theone.model

import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.*
import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobUser
import com.example.a14512.theone.R
import com.example.a14512.theone.STATUS_VERIFY_NONE
import com.example.a14512.theone.STATUS_VERIFY_READED
import com.example.a14512.theone.TheOneApplication
import org.json.JSONObject

/**
 * @author 14512 on 2018/5/2
 */
data class NewFriend(var id: Long?, var uid: String?, var msg: String?, var avatar: String?,
                     var name: String?, var status: Int?, var time: Long?) {
    constructor(): this(null)

    constructor(id: Long?): this(id, null, null, null, null, null, null)

}

class Friend : BmobObject() {
    private lateinit var mUser: User
    private lateinit var mFriendUser: User

    @Transient
    private lateinit var mPinyin: String

    fun getPinyin(): String {
        return mPinyin
    }

    fun setPinyin(pinyin: String) {
        this.mPinyin = pinyin
    }

    fun getUser(): User {
        return mUser
    }

    fun setUser(user: User) {
        this.mUser = user
    }

    fun getFriendUser(): User {
        return  mFriendUser
    }

    fun setFriendUser(friendUser: User) {
        this.mFriendUser = friendUser
    }


}

class User(): BmobUser() {
    private var avatar: String? = null

    constructor(friend: NewFriend) : this() {
        objectId = friend.uid
        username = friend.name
        setAvatar(friend.avatar)
    }

    fun getAvatar(): String? {
        return avatar
    }

    fun setAvatar(avatar: String?) {
        this.avatar = avatar
    }

}

class PrivateConversation(private var conversation: BmobIMConversation): Conversation() {
    private var lastMsg: BmobIMMessage? = null

    init {
        cType = BmobIMConversationType.setValue(conversation.conversationType)
        cId = conversation.conversationId
        if (cType == BmobIMConversationType.PRIVATE) {
            cName = conversation.conversationTitle
            if (cName.isEmpty()) cName = cId
        } else {
            cName = "未知会话"
        }

        var msgs = conversation.messages
        if (msgs != null && msgs.size > 0) {
            lastMsg = msgs[0]
        }
    }

    override fun getAvatar(): Any {
        return if (cType == BmobIMConversationType.PRIVATE) {
            var avatar = conversation.conversationIcon
            if (avatar.isEmpty()) {
                //头像为空，使用默认头像
                R.mipmap.ic_launcher_round
            } else {
                avatar as Any
            }
        } else {
            R.mipmap.ic_launcher_round
        }
    }

    override fun getLastMessageTime(): Long {
        return if (lastMsg != null) {
            lastMsg!!.createTime
        } else {
            0
        }
    }

    override fun getLastMessageContent(): String {
        return if (lastMsg != null) {
            var content = lastMsg!!.content
            when (lastMsg!!.msgType) {
                BmobIMMessageType.TEXT.type, "agree" -> content

                BmobIMMessageType.IMAGE.type -> "[图片]"

                BmobIMMessageType.VOICE.type -> "[语音]"

                BmobIMMessageType.LOCATION.type -> "[位置]"

                BmobIMMessageType.VIDEO.type -> "[视频]"

                else -> "[未知]"
            }
        } else {
            ""
        }
    }

    override fun getUnReadCount(): Int {
        return BmobIM.getInstance().getUnReadCount(conversation.conversationId).toInt()
    }

    override fun readAllMessage() {
        conversation.updateLocalCache()
    }

    fun getConversation(): BmobIMConversation {
        return conversation
    }

}

class NewFriendConversation(friend: NewFriend): Conversation() {
    private var lastFriend: NewFriend = friend

    init {
        this.cName = "新朋友"
    }

    override fun getAvatar(): Any {
        return R.mipmap.new_friends_icon
    }

    override fun getLastMessageTime(): Long {
        return if (lastFriend != null) {
            lastFriend.time!!
        } else {
            0
        }
    }

    override fun getLastMessageContent(): String {
        return if (lastFriend != null) {
            val status = lastFriend.status
            var name = lastFriend.name
            if (name != null && name.isEmpty()) {
                name = lastFriend.uid
            }
            //目前的好友请求都是别人发给我的
            if (status == null || status === STATUS_VERIFY_NONE || status === STATUS_VERIFY_READED) {
                name + "请求添加好友"
            } else {
                "我已添加$name"
            }
        } else {
            ""
        }
    }

    override fun getUnReadCount(): Int {
        return NewFriendManager.getInstance(TheOneApplication.getContext()).getNewInvitationCount()
    }

    override fun readAllMessage() {
        //批量更新未读未认证的消息为已读状态
        NewFriendManager.getInstance(TheOneApplication.getContext()).updateBatchStatus()
    }

    fun getLastFriend(): NewFriend {
        return lastFriend
    }

}

class AddFriendMsg : BmobIMExtraMessage() {
    companion object {
        const val ADD = "add"

        fun convert(msg: BmobIMMessage): NewFriend {
            val add = NewFriend()
            add.msg = msg.content
            add.time = msg.createTime
            add.status = STATUS_VERIFY_NONE
            try {
                if (msg.extra.isNotEmpty()) {
                    val json = JSONObject(msg.extra)
                    val name = json["name"]
                    val avatar = json["avatar"]
                    val uid = json["uid"]
                    add.name = name as String?
                    add.avatar = avatar as String?
                    add.uid = uid as String?

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return add
        }
    }

    override fun getMsgType(): String {
        return ADD
    }

    override fun isTransient(): Boolean {
        //设置为true,表明为暂态消息，那么这条消息并不会保存到本地db中，SDK只负责发送出去
        //设置为false,则会保存到指定会话的数据库中
        return true
    }
}

class AgreeFriendMsg(): BmobIMExtraMessage() {


    //以下均是从extra里面抽离出来的字段，方便获取
    private var uid: String? = null//最初的发送方
    private var time: Long? = null
    private var msg: String? = null//用于通知栏显示的内容

    constructor(msg: BmobIMMessage): this() {
        super.parse(msg)
    }

    companion object {
        const val AGREE = "agree"

        fun convert(msg: BmobIMMessage): AgreeFriendMsg {
            val agree = AgreeFriendMsg(msg)
            try {
                if (msg.extra.isNotEmpty()) {
                    val json = JSONObject(msg.extra)
                    val time = json.getLong("time")
                    val uid = json.getString("uid")
                    val m = json.getString("msg")
                    agree.setMsg(m)
                    agree.setUid(uid)
                    agree.setTime(time)
                }
            } catch (e: Exception) {
                e.printStackTrace();
            }
            return agree
        }

    }

    fun getUid(): String? {
        return uid
    }

    fun setUid(uid: String) {
        this.uid = uid
    }

    fun getTime(): Long? {
        return time
    }

    fun setTime(time: Long?) {
        this.time = time
    }

    fun getMsg(): String? {
        return msg
    }

    fun setMsg(msg: String) {
        this.msg = msg
    }

    override fun getMsgType(): String {
        return AGREE
    }

    override fun isTransient(): Boolean {
        //如果需要在对方的会话表中新增一条该类型的消息，则设置为false，表明是非暂态会话
        //此处将同意添加好友的请求设置为false，为了演示怎样向会话表和消息表中新增一个类型
        return false
    }
}
