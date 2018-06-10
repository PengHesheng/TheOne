package com.example.a14512.theone.presenter

import android.content.Context
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMConversation
import cn.bmob.newim.bean.BmobIMMessage
import cn.bmob.newim.bean.BmobIMUserInfo
import cn.bmob.newim.core.BmobIMClient
import cn.bmob.newim.listener.MessageSendListener
import cn.bmob.v3.exception.BmobException
import com.example.a14512.theone.model.AddFriendMsg
import com.example.a14512.theone.model.UserModel
import com.example.a14512.theone.view.IUserInfoView
import java.util.*

/**
 * @author 14512 on 2018/5/31
 */
class UserInfoPresenterImp(private val mContext: Context,
                           private val mView: IUserInfoView) : IUserInfoPresenter {
    override fun sendAddFriendMsg(info: BmobIMUserInfo) {
        val conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null)
        val msgManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance)
        val msg = AddFriendMsg()
        val currentUser = UserModel.getInstance().getCurrentUser()
        if (currentUser != null) {
            msg.content = "很高兴认识你，可以加个好友吗?"
            //这里只是举个例子，其实可以不需要传发送者的信息过去
            val map = HashMap<String, Any>()
            map["name"] = currentUser.username  //发送者姓名
            map["avatar"] = currentUser.getAvatar() ?: ""  //发送者的头像
            map["uid"] = currentUser.objectId  //发送者的uid
            msg.setExtraMap(map)
            msgManager.sendMessage(msg, object : MessageSendListener() {
                override fun done(msg: BmobIMMessage, e: BmobException?) {
                    if (e == null) {//发送成功
                        mView.toastMsg("好友请求发送成功，等待验证")
                    } else {//发送失败
                        mView.toastMsg("发送失败:" + e.message)
                    }
                }
            })
        }
    }

}