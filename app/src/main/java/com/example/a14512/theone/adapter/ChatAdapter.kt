package com.example.a14512.theone.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cn.bmob.newim.bean.BmobIMConversation
import cn.bmob.newim.bean.BmobIMMessage
import cn.bmob.newim.bean.BmobIMMessageType
import cn.bmob.newim.db.dao.`MessageDao$Properties`.MsgType
import cn.bmob.v3.BmobUser
import com.example.a14512.theone.model.Conversation
import java.util.*

/**
 * @author 14512 on 2018/5/29
 */
class ChatAdapter : BaseAdapter<RecyclerView.ViewHolder>() {
    private lateinit var mContext: Context
    private lateinit var mListener: OnRecyclerItemClickListener
    private var mMsgs: ArrayList<BmobIMMessage> = ArrayList()
    private lateinit var mConversation: BmobIMConversation
    private var currentUid = ""
    //显示时间间隔:10分钟
    private val TIME_INTERVAL = 10 * 60 * 1000
    //文本
    private val TYPE_RECEIVER_TXT = 0
    private val TYPE_SEND_TXT = 1
    //图片
    private val TYPE_SEND_IMAGE = 2
    private val TYPE_RECEIVER_IMAGE = 3
    //位置
    private val TYPE_SEND_LOCATION = 4
    private val TYPE_RECEIVER_LOCATION = 5,
    //语音
    private val TYPE_SEND_VOICE = 6
    private val TYPE_RECEIVER_VOICE = 7
    //视频
    private val TYPE_SEND_VIDEO = 8
    private val TYPE_RECEIVER_VIDEO = 9
    //同意添加好友成功后的样式
    private val TYPE_AGREE = 10

    init {
        try {
            currentUid = BmobUser.getCurrentUser().objectId
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setConversation(conversation: BmobIMConversation) {
        mConversation = conversation
    }

    fun addMsg(msgs: List<BmobIMMessage>) {
        mMsgs.addAll(0, msgs)
        notifyDataSetChanged()
    }

    fun addMsg(msg: BmobIMMessage) {
        mMsgs.addAll(Collections.singletonList(msg))
        notifyDataSetChanged()
    }

    fun deleteMsg(position: Int) {
        mMsgs.removeAt(position)
        notifyDataSetChanged()
    }

    fun findPosition(msg: BmobIMMessage): Int {
        return mMsgs.indexOf(msg)
    }

    fun getFirstMsg(): BmobIMMessage? {
        return if (mMsgs.isNotEmpty()) {
            mMsgs[0]
        } else {
            null
        }
    }

    fun findPosition(id: Long) {
        //TODO 根据id找position
        return
    }


    override fun setOnItemClickListener(listener: BaseClickListener) {
        mListener = listener as OnRecyclerItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
    }

    override fun getItemCount(): Int {
        return mMsgs.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemViewType(position: Int): Int {
        val msg = mMsgs[position]
        return when(msg.msgType) {
            BmobIMMessageType.TEXT.type -> if (msg.fromId == currentUid) TYPE_SEND_TXT else TYPE_RECEIVER_TXT
            BmobIMMessageType.IMAGE.type -> if (msg.fromId == currentUid) TYPE_SEND_IMAGE else TYPE_RECEIVER_IMAGE
            BmobIMMessageType.LOCATION.type -> if (msg.fromId == currentUid) TYPE_SEND_LOCATION else TYPE_RECEIVER_LOCATION

            else -> {
                -1
            }
        }
    }

    class SendTextHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }


}