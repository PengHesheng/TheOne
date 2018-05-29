package com.example.a14512.theone.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import cn.bmob.newim.bean.BmobIMConversation
import cn.bmob.newim.bean.BmobIMMessage
import cn.bmob.newim.bean.BmobIMMessageType
import cn.bmob.v3.BmobUser
import com.example.a14512.theone.R
import kotlinx.android.synthetic.main.item_recycler_chat_agree.view.*
import kotlinx.android.synthetic.main.item_recycler_chat_receive_text.view.*
import kotlinx.android.synthetic.main.item_recycler_chat_send_img.view.*
import kotlinx.android.synthetic.main.item_recycler_chat_send_text.view.*
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
    private val TYPE_RECEIVER_LOCATION = 5
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
        return when(viewType) {
            TYPE_SEND_TXT -> SendTextHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_send_text, parent, false))
            TYPE_RECEIVER_TXT -> ReceiveTextHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_receive_text, parent, false))
            TYPE_SEND_IMAGE -> SendImageHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_send_img, parent, false))
            TYPE_RECEIVER_IMAGE -> ReceiveImageHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_receive_img, parent, false))
            TYPE_SEND_VOICE -> SendVoiceHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_send_voice, parent, false))
            TYPE_RECEIVER_VOICE -> ReceiveVoiceHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_receive_voice, parent, false))
            TYPE_SEND_LOCATION -> SendLocationHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_send_location, parent, false))
            TYPE_RECEIVER_LOCATION -> ReceiveLocationHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_receive_location, parent, false))
            TYPE_SEND_VIDEO -> SendVideoHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_send_text, parent, false))
            TYPE_RECEIVER_VIDEO -> ReceiveVideoHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_receive_text, parent, false))
            else -> AgreeHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_chat_agree, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return mMsgs.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = mMsgs[position]
        when (holder) {
            is SendTextHolder -> {

            }
            is ReceiveTextHolder -> {

            }
            is SendImageHolder -> {

            }
            is ReceiveImageHolder -> {

            }
            is SendVoiceHolder -> {

            }
            is ReceiveVoiceHolder -> {

            }
            is SendLocationHolder -> {

            }
            is ReceiveLocationHolder -> {

            }
            is SendVideoHolder -> {

            }
            is ReceiveVideoHolder -> {

            }
            is AgreeHolder -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val msg = mMsgs[position]
        return when(msg.msgType) {
            BmobIMMessageType.TEXT.type -> if (msg.fromId == currentUid) TYPE_SEND_TXT else TYPE_RECEIVER_TXT
            BmobIMMessageType.IMAGE.type -> if (msg.fromId == currentUid) TYPE_SEND_IMAGE else TYPE_RECEIVER_IMAGE
            BmobIMMessageType.LOCATION.type -> if (msg.fromId == currentUid) TYPE_SEND_LOCATION else TYPE_RECEIVER_LOCATION
            BmobIMMessageType.VOICE.type -> if (msg.fromId == currentUid) TYPE_SEND_VOICE else TYPE_RECEIVER_VOICE
            BmobIMMessageType.VIDEO.type -> if (msg.fromId == currentUid) TYPE_SEND_VIDEO else TYPE_SEND_VIDEO
            "agree" -> TYPE_AGREE
            else -> {
                -1
            }
        }
    }

    class SendTextHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTime: TextView
        lateinit var ivPortrait: ImageView
        lateinit var tvContent: TextView
        lateinit var ivFailed: ImageView
        lateinit var tvStatus: TextView
        lateinit var progress: ProgressBar

        init {
            if (itemView != null) {
                tvTime = itemView.tvItemTimeChatSendText
                ivPortrait = itemView.ivItemPortraitChatSendText
                tvContent = itemView.tvItemContentChatSendText
                ivFailed = itemView.ivItemFailedChatSendText
                tvStatus = itemView.tvItemStatusChatSendText
                progress = itemView.progressItemLoadChanSendText
            }
        }
    }

    class ReceiveTextHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private lateinit var tvTime: TextView
        private lateinit var ivPortrait: ImageView
        private lateinit var tvContent: TextView

        init {
            if (itemView != null) {
                tvTime = itemView.tvItemTimeChatReceiveText
                ivPortrait = itemView.ivItemPortraitChatReceiveText
                tvContent = itemView.tvItemContentChatReceiveText
            }
        }
    }

    class SendImageHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTime: TextView
        lateinit var ivPortrait: ImageView
        lateinit var ivContent: ImageView
        lateinit var ivFailed: ImageView
        lateinit var tvStatus: TextView
        lateinit var progress: ProgressBar

        init {
            if (itemView != null) {
                tvTime = itemView.tvItemTimeChatSendImg
                ivPortrait = itemView.ivItemPortraitChatSendImg
                ivContent = itemView.ivItemContentChatSendImg
                ivFailed = itemView.ivItemFailedChatSendImg
                tvStatus = itemView.tvItemStatusChatSendImg
                progress = itemView.progressItemLoadChanSendImg
            }
        }
    }

    class ReceiveImageHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

    class SendVoiceHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

    class ReceiveVoiceHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

    class SendVideoHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

    class ReceiveVideoHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

    class SendLocationHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

    class ReceiveLocationHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

    class AgreeHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTime: TextView
        lateinit var tvContent: TextView

        init {
            if (itemView != null) {
                tvTime = itemView.tvItemTimeChatAgree
                tvContent = itemView.tvItemContentChatAgree
            }
        }
    }

}