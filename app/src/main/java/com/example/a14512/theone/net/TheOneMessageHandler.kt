package com.example.a14512.theone.net

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import cn.bmob.newim.bean.BmobIMMessage
import cn.bmob.newim.bean.BmobIMMessageType
import cn.bmob.newim.bean.BmobIMUserInfo
import cn.bmob.newim.event.MessageEvent
import cn.bmob.newim.event.OfflineMessageEvent
import cn.bmob.newim.listener.BmobIMMessageHandler
import cn.bmob.newim.notification.BmobNotificationManager
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.example.a14512.theone.R
import com.example.a14512.theone.event.RefreshEvent
import com.example.a14512.theone.model.*
import com.example.a14512.theone.model.i.UpdateCacheListener
import com.example.a14512.theone.utils.PLog
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.activity.MainActivity
import org.greenrobot.eventbus.EventBus

/**
 * @author 14512 on 2018/5/1
 */
class TheOneMessageHandler(private val context: Context) : BmobIMMessageHandler() {

    override fun onMessageReceive(event: MessageEvent?) {
        //在线消息
        //当接收到服务器发来的消息时，此方法被调用
        if (event != null) {
            executeMessage(event)
        }
    }

    private fun executeMessage(event: MessageEvent) {
        UserModel.getInstance().updateUserInfo(event, object : UpdateCacheListener() {
            override fun done(e: BmobException?) {
                var msg = event.message
                if (BmobIMMessageType.getMessageTypeValue(msg.msgType) ==0) {
                    //自定义消息类型：0
                    processCustomMessage(msg, event.fromUserInfo)
                } else {
                    //SDK内部内部支持的消息类型
                    processSDKMessage(msg, event)
                }
            }
        })
    }

    private fun processSDKMessage(msg: BmobIMMessage?, event: MessageEvent) {

    }

    private fun processCustomMessage(msg: BmobIMMessage?, info: BmobIMUserInfo?) {
        if (msg != null && info != null) {
            var type = msg.msgType
            EventBus.getDefault().post(RefreshEvent())
            when(type) {
                AddFriendMsg.ADD -> {
                    val newFriend = AddFriendMsg.convert(msg)
                    val id = NewFriendManager.getInstance(context).insertOrUpdateNewFriend(newFriend)
                    if (id > 0) {
                        showAddNotify(newFriend)
                    }
                }

                AgreeFriendMsg.AGREE -> {
                    //接收到的对方同意添加自己为好友,此时需要做的事情：1、添加对方为好友，2、显示通知
                    val agree = AgreeFriendMsg.convert(msg)
                    addFriend(agree.getFromId())//添加消息的发送方为好友
                    //这里应该也需要做下校验--来检测下是否已经同意过该好友请求，我这里省略了
                    showAgreeNotify(info, agree)
                }

                else -> ToastUtil.show(context, "接收到的自定义消息：${msg.msgType}${msg.content} ${msg.extra}")
            }
        }
    }

    private fun showAgreeNotify(info: BmobIMUserInfo, agree: AgreeFriendMsg) {
        val pendingIntent = Intent(context, MainActivity::class.java)
        pendingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
        BmobNotificationManager.getInstance(context).showNotification(largeIcon, info.name,
                agree.getMsg(), agree.getMsg(), pendingIntent)

    }

    private fun addFriend(uid: String?) {
        val user = User()
        user.objectId = uid
        UserModel.getInstance()
                .agreeAddFriend(user, object : SaveListener<String>() {
                    override fun done(s: String, e: BmobException?) {
                        if (e == null) {
                            PLog.e("success")
                        } else {
                            PLog.e(e.message.toString())
                        }
                    }
                })
    }

    private fun showAddNotify(friend: NewFriend) {
        val pendingIntent = Intent(context, MainActivity::class.java)
        pendingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        //这里可以是应用图标，也可以将聊天头像转成bitmap
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
        BmobNotificationManager.getInstance(context).showNotification(largeIcon,
                friend.name, friend.msg, "${friend.name}请求添加你为朋友", pendingIntent)
    }

    override fun onOfflineReceive(event: OfflineMessageEvent?) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
        if (event != null) {
            val map: Map<String, List<MessageEvent>> = event.eventMap
            //挨个检测下离线消息所属的用户的信息是否需要更新
            var list: List<MessageEvent>
            for (entry in map.entries) {
                list = entry.value
                for (i in 0 until list.size) {
                    //处理每条消息
                    executeMessage(list[i])
                }
            }
        }
    }
}