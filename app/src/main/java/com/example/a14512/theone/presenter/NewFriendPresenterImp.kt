package com.example.a14512.theone.presenter

import android.content.Context
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMConversation
import cn.bmob.newim.bean.BmobIMMessage
import cn.bmob.newim.bean.BmobIMUserInfo
import cn.bmob.newim.core.BmobIMClient
import cn.bmob.newim.listener.MessageSendListener
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.example.a14512.theone.STATUS_VERIFIED
import com.example.a14512.theone.STATUS_VERIFY_REFUSE
import com.example.a14512.theone.model.*
import com.example.a14512.theone.view.INewFriendView

/**
 * @author 14512 on 2018/6/8
 */
class NewFriendPresenterImp(private val mContext: Context,
                            private val mView: INewFriendView) : INewFriendPresenter {
    override fun rejectNewFriend(newFriend: NewFriend) {
        agreeAddFriend(newFriend, "对不起，对方拒绝了你的请求！", STATUS_VERIFY_REFUSE)
    }

    override fun deleteNewMsg(newFriend: NewFriend) {
        NewFriendManager.getInstance(mContext).deleteNewFriend(newFriend)
    }

    override fun agreeNewFriend(newFriend: NewFriend) {
        agreeAddFriend(newFriend, "我通过了你的好友验证请求，我们可以开始聊天了!", STATUS_VERIFIED)
    }

    private fun agreeAddFriend(newFriend: NewFriend, content: String?, status: Int) {
        val user = User()
        user.objectId = newFriend.uid
        UserModel.getInstance().agreeAddFriend(user, object : SaveListener<String>() {
            override fun done(p0: String?, p1: BmobException?) {
                if (p1 == null) {
                    sendAgreeAddMsg(newFriend, content, status)
                } else {
                    mView.toastMsg(p1.message.toString())
                }
            }
        })
    }

    private fun sendAgreeAddMsg(newFriend: NewFriend, content: String?, status: Int) {
        val info = BmobIMUserInfo(newFriend.uid, newFriend.name, newFriend.avatar)
        val conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null)
        val msgManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance)
        val addMsg = AgreeFriendMsg()
        val currentUser = UserModel.getInstance().getCurrentUser()
        addMsg.content = content
        msgManager.sendMessage(addMsg, object : MessageSendListener() {
            override fun done(p0: BmobIMMessage?, p1: BmobException?) {
                if (p1 == null) {
                    NewFriendManager.getInstance(mContext).updateNewFriend(newFriend, status)
                } else {
                    mView.toastMsg(p1.message.toString())
                }
            }
        })
    }

    override fun getAllNewFriend() {
        mView.setAdapter(NewFriendManager.getInstance(mContext).getAllNewFriend() as ArrayList<NewFriend>)
    }


}