package com.example.a14512.theone.presenter

import android.content.Context
import cn.bmob.newim.BmobIM
import com.example.a14512.theone.model.Conversation
import com.example.a14512.theone.model.NewFriendConversation
import com.example.a14512.theone.model.PrivateConversation
import com.example.a14512.theone.model.dao.NewFriendManager
import com.example.a14512.theone.view.IConversationView
import java.util.*

/**
 * @author 14512 on 2018/5/28
 */
class ConversationPresenterImp(private val mContext: Context,
                               private val mView: IConversationView) : IConversationPresenter {
    override fun getConversation() {
        val conversations: ArrayList<Conversation> = ArrayList()
        val bmobConversations = BmobIM.getInstance().loadAllConversation()
        if (bmobConversations != null && bmobConversations.isNotEmpty()) {
            for (conversation in bmobConversations) {
                when (conversation.conversationType) {
                    1 -> conversations.add(PrivateConversation(conversation))
                    else -> { }
                }
            }
        }

        //添加新朋友会话-获取好友请求表中最新一条记录
        val newFriends = NewFriendManager.getInstance(mContext).allNewFriend
        if (newFriends != null && newFriends.isNotEmpty()) {
            conversations.add(NewFriendConversation(newFriends[0]))
        }
        conversations.sort()
        mView.setAdapter(conversations)
    }
}