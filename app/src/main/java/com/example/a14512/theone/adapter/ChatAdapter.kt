package com.example.a14512.theone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import cn.bmob.newim.bean.*
import cn.bmob.newim.core.BmobDownloadManager
import cn.bmob.newim.listener.FileDownloadListener
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import com.bumptech.glide.Glide
import com.example.a14512.theone.R
import com.example.a14512.theone.utils.TimeUtil
import kotlinx.android.synthetic.main.item_chat_agree_recycler.view.*
import kotlinx.android.synthetic.main.item_chat_receive_img_recycler.view.*
import kotlinx.android.synthetic.main.item_chat_receive_location_recycler.view.*
import kotlinx.android.synthetic.main.item_chat_receive_text_recycler.view.*
import kotlinx.android.synthetic.main.item_chat_receive_voice_recycler.view.*
import kotlinx.android.synthetic.main.item_chat_send_img_recycler.view.*
import kotlinx.android.synthetic.main.item_chat_send_location_recycler.view.*
import kotlinx.android.synthetic.main.item_chat_send_text_recycler.view.*
import kotlinx.android.synthetic.main.item_chat_send_voice_recycler.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author 14512 on 2018/5/29
 */
class ChatAdapter : BaseAdapter<RecyclerView.ViewHolder>() {
    private lateinit var mContext: Context
    private lateinit var mListener: OnRecyclerChatItemClickListener
    private var mMsgs: ArrayList<BmobIMMessage> = ArrayList()
    private lateinit var mConversation: BmobIMConversation
    private var currentUid = ""
    //显示时间间隔:10分钟
    private val TIME_INTERVAL = 10 * 60 * 1000
    //文本
    val TYPE_RECEIVER_TXT = 0
    val TYPE_SEND_TXT = 1
    //图片
    val TYPE_SEND_IMAGE = 2
    val TYPE_RECEIVER_IMAGE = 3
    //位置
    val TYPE_SEND_LOCATION = 4
    val TYPE_RECEIVER_LOCATION = 5
    //语音
    val TYPE_SEND_VOICE = 6
    val TYPE_RECEIVER_VOICE = 7
    //视频
    val TYPE_SEND_VIDEO = 8
    val TYPE_RECEIVER_VIDEO = 9
    //同意添加好友成功后的样式
    val TYPE_AGREE = 10

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

    fun getMsgs(): ArrayList<BmobIMMessage> {
        return mMsgs
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

    private fun shouldShowTime(position: Int): Boolean {
        if (position == 0) {
            return true
        }
        val lastTime = mMsgs[position - 1].createTime
        val curTime = mMsgs[position].createTime
        return curTime - lastTime > TIME_INTERVAL
    }

    override fun setOnItemClickListener(listener: BaseClickListener) {
        mListener = listener as OnRecyclerChatItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return when(viewType) {
            TYPE_SEND_TXT -> SendTextHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_send_text_recycler, parent, false))
            TYPE_RECEIVER_TXT -> ReceiveTextHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_receive_text_recycler, parent, false))
            TYPE_SEND_IMAGE -> SendImageHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_send_img_recycler, parent, false))
            TYPE_RECEIVER_IMAGE -> ReceiveImageHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_receive_img_recycler, parent, false))
            TYPE_SEND_VOICE -> SendVoiceHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_send_voice_recycler, parent, false))
            TYPE_RECEIVER_VOICE -> ReceiveVoiceHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_receive_voice_recycler, parent, false))
            TYPE_SEND_LOCATION -> SendLocationHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_send_location_recycler, parent, false))
            TYPE_RECEIVER_LOCATION -> ReceiveLocationHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_receive_location_recycler, parent, false))
            TYPE_SEND_VIDEO -> SendVideoHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_send_text_recycler, parent, false))
            TYPE_RECEIVER_VIDEO -> ReceiveVideoHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_receive_text_recycler, parent, false))
            else -> AgreeHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_chat_agree_recycler, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return mMsgs.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = mMsgs[position]
        when (holder) {
            is SendTextHolder -> {
                bindSendText(holder, msg, position)
            }
            is ReceiveTextHolder -> {
                bindReceiveText(holder, msg, position)
            }
            is SendImageHolder -> {
                bindSendImg(holder, msg, position)
            }
            is ReceiveImageHolder -> {
                bindReceiveImg(holder, msg, position)
            }
            is SendVoiceHolder -> {
                bindSendVoice(holder, msg, position)
            }
            is ReceiveVoiceHolder -> {
                bindReceiveVoice(holder, msg, position)
            }
            is SendLocationHolder -> {
                bindSendLocation(holder, msg, position)
            }
            is ReceiveLocationHolder -> {
                bindReceiveLocation(holder, msg, position)
            }
            is SendVideoHolder -> {
                bindSendVideo(holder, msg, position)
            }
            is ReceiveVideoHolder -> {
                bindReceiveVideo(holder, msg, position)
            }
            is AgreeHolder -> {
                bindAgree(holder, msg, position)
            }
        }
    }

    private fun bindAgree(holder: AgreeHolder, msg: BmobIMMessage, position: Int) {
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        holder.tvContent.text = msg.content
    }

    @SuppressLint("SetTextI18n")
    private fun bindReceiveVideo(holder: ReceiveVideoHolder, msg: BmobIMMessage, position: Int) {
        val info = msg.bmobIMUserInfo
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        Glide.with(mContext).load(info?.avatar)
                .error(Glide.with(mContext).load(R.mipmap.default_portrait))
                .into(holder.ivPortrait)
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        holder.tvContent.text = "接收到的视频文件${msg.content}"

        if (mListener != null) {
            holder.ivPortrait.setOnClickListener {
                mListener.onPortraitClick(it, position)
            }
            holder.ivPortrait.setOnLongClickListener {
                mListener.onPortraitLongClick(it, position)
            }
            holder.tvContent.setOnClickListener {
                mListener.onContentClick(it, position)
            }
            holder.tvContent.setOnLongClickListener {
                mListener.onContentLongClick(it, position)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindSendVideo(holder: SendVideoHolder, msg: BmobIMMessage, position: Int) {
        val info = msg.bmobIMUserInfo
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        Glide.with(mContext).load(info?.avatar)
                .error(Glide.with(mContext).load(R.mipmap.default_portrait))
                .into(holder.ivPortrait)
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        holder.tvContent.text = "发送的视频文件${msg.content}"

        when(msg.sendStatus) {
            BmobIMSendStatus.SEND_FAILED.status -> {
                holder.ivFailed.visibility = View.VISIBLE
                holder.progress.visibility = View.GONE
            }
            BmobIMSendStatus.SENDING.status -> {
                holder.ivFailed.visibility = View.GONE
                holder.progress.visibility = View.VISIBLE
            }
            else -> {
                holder.ivFailed.visibility = View.GONE
                holder.progress.visibility = View.GONE
            }
        }

        if (mListener != null) {
            holder.ivPortrait.setOnClickListener {
                mListener.onPortraitClick(it, position)
            }
            holder.ivPortrait.setOnLongClickListener {
                mListener.onPortraitLongClick(it, position)
            }
            holder.tvContent.setOnClickListener {
                mListener.onContentClick(it, position)
            }
            holder.tvContent.setOnLongClickListener {
                mListener.onContentLongClick(it, position)
            }
            holder.ivFailed.setOnClickListener {
                mListener.onResendClick(it, holder.tvStatus, holder.progress, position)
            }
        }
    }

    private fun bindReceiveLocation(holder: ReceiveLocationHolder, msg: BmobIMMessage, position: Int) {
        val info = msg.bmobIMUserInfo
        val message = BmobIMLocationMessage.buildFromDB(msg)
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        Glide.with(mContext).load(info?.avatar)
                .error(Glide.with(mContext).load(R.mipmap.default_portrait))
                .into(holder.ivPortrait)
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        holder.tvContent.text = message.address

        if (mListener != null) {
            holder.ivPortrait.setOnClickListener {
                mListener.onPortraitClick(it, position)
            }
            holder.ivPortrait.setOnLongClickListener {
                mListener.onPortraitLongClick(it, position)
            }
            holder.tvContent.setOnClickListener {
                mListener.onContentClick(it, position)
            }
            holder.tvContent.setOnLongClickListener {
                mListener.onContentLongClick(it, position)
            }
        }
    }

    private fun bindSendLocation(holder: SendLocationHolder, msg: BmobIMMessage, position: Int) {
        val info = msg.bmobIMUserInfo
        val message = BmobIMLocationMessage.buildFromDB(msg)
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        Glide.with(mContext).load(info?.avatar)
                .error(Glide.with(mContext).load(R.mipmap.default_portrait))
                .into(holder.ivPortrait)
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        holder.tvContent.text = message.address

        when(message.sendStatus) {
            BmobIMSendStatus.SEND_FAILED.status -> {
                holder.ivFailed.visibility = View.VISIBLE
                holder.progress.visibility = View.GONE
            }
            BmobIMSendStatus.SENDING.status -> {
                holder.ivFailed.visibility = View.GONE
                holder.progress.visibility = View.VISIBLE
            }
            else -> {
                holder.ivFailed.visibility = View.GONE
                holder.progress.visibility = View.GONE
            }
        }

        if (mListener != null) {
            holder.ivPortrait.setOnClickListener {
                mListener.onPortraitClick(it, position)
            }
            holder.ivPortrait.setOnLongClickListener {
                mListener.onPortraitLongClick(it, position)
            }
            holder.tvContent.setOnClickListener {
                mListener.onContentClick(it, position)
            }
            holder.tvContent.setOnLongClickListener {
                mListener.onContentLongClick(it, position)
            }
            holder.ivFailed.setOnClickListener {
                mListener.onResendClick(it, holder.tvStatus, holder.progress, position)
            }
        }
    }

    private fun bindReceiveVoice(holder: ReceiveVoiceHolder, msg: BmobIMMessage, position: Int) {
        val info = msg.bmobIMUserInfo
        val message = BmobIMAudioMessage.buildFromDB(true, msg)
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        Glide.with(mContext).load(info?.avatar)
                .error(Glide.with(mContext).load(R.mipmap.default_portrait))
                .into(holder.ivPortrait)
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        val isExists = BmobDownloadManager.isAudioExist(currentUid, message)
        if (!isExists) {//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
            val downloadTask = BmobDownloadManager(mContext, msg, object : FileDownloadListener() {

                override fun onStart() {
                    holder.progrss.visibility = View.VISIBLE
                    holder.tvLength.visibility = View.GONE
                    holder.ivVoice.visibility = View.INVISIBLE//只有下载完成才显示播放的按钮
                }

                override fun done(e: BmobException?) {
                    if (e == null) {
                        holder.progrss.visibility = View.GONE
                        holder.tvLength.visibility = View.VISIBLE
                        holder.tvLength.text = message.duration.toString()
                        holder.ivVoice.visibility = View.VISIBLE
                    } else {
                        holder.progrss.visibility = View.GONE
                        holder.tvLength.visibility = View.GONE
                        holder.ivVoice.visibility = View.INVISIBLE
                    }
                }
            })
            downloadTask.execute(message.content)
        } else {
            holder.tvLength.visibility = View.VISIBLE
            holder.tvLength.text = message.duration.toString()
        }

        if (mListener != null) {
            holder.ivPortrait.setOnClickListener {
                mListener.onPortraitClick(it, position)
            }
            holder.ivPortrait.setOnLongClickListener {
                mListener.onPortraitLongClick(it, position)
            }
            holder.ivVoice.setOnClickListener {
                mListener.onContentClick(it, position)
            }
            holder.ivVoice.setOnLongClickListener {
                mListener.onContentLongClick(it, position)
            }
        }
    }

    private fun bindSendVoice(holder: SendVoiceHolder, msg: BmobIMMessage, position: Int) {
        val info = msg.bmobIMUserInfo
        val message = BmobIMAudioMessage.buildFromDB(true, msg)
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        Glide.with(mContext).load(info?.avatar)
                .error(Glide.with(mContext).load(R.mipmap.default_portrait))
                .into(holder.ivPortrait)
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        holder.tvLength.text = message.duration.toString()

        when(message.sendStatus) {
            BmobIMSendStatus.SEND_FAILED.status, BmobIMSendStatus.UPLOAD_FAILED.status -> {
                holder.ivFailed.visibility = View.VISIBLE
                holder.progress.visibility = View.GONE
                holder.tvStatus.visibility = View.GONE
                holder.tvLength.visibility = View.GONE
            }
            BmobIMSendStatus.SENDING.status -> {
                holder.ivFailed.visibility = View.GONE
                holder.progress.visibility = View.VISIBLE
                holder.tvStatus.visibility = View.GONE
                holder.tvLength.visibility = View.GONE
            }
            else -> {
                holder.ivFailed.visibility = View.GONE
                holder.progress.visibility = View.GONE
                holder.tvStatus.visibility = View.GONE
                holder.tvLength.visibility = View.VISIBLE
            }
        }

        if (mListener != null) {
            holder.ivPortrait.setOnClickListener {
                mListener.onPortraitClick(it, position)
            }
            holder.ivPortrait.setOnLongClickListener {
                mListener.onPortraitLongClick(it, position)
            }
            holder.ivVoice.setOnClickListener {
                mListener.onContentClick(it, position)
            }
            holder.ivVoice.setOnLongClickListener {
                mListener.onContentLongClick(it, position)
            }
            holder.ivFailed.setOnClickListener {
                mListener.onResendClick(it, holder.tvStatus, holder.progress, position)
            }
        }
    }

    private fun bindReceiveImg(holder: ReceiveImageHolder, msg: BmobIMMessage, position: Int) {
        val info = msg.bmobIMUserInfo
        val message = BmobIMImageMessage.buildFromDB(true, msg)
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        Glide.with(mContext).load(info?.avatar)
                .error(Glide.with(mContext).load(R.mipmap.default_portrait))
                .into(holder.ivPortrait)
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        Glide.with(mContext).load(message.remoteUrl)
                .error(Glide.with(mContext).load(R.mipmap.iv_load_failure))
                .into(holder.ivContent)

        if (mListener != null) {
            holder.ivPortrait.setOnClickListener {
                mListener.onPortraitClick(it, position)
            }
            holder.ivPortrait.setOnLongClickListener {
                mListener.onPortraitLongClick(it, position)
            }
            holder.ivContent.setOnClickListener {
                mListener.onContentClick(it, position)
            }
            holder.ivContent.setOnLongClickListener {
                mListener.onContentLongClick(it, position)
            }
        }
    }

    private fun bindSendImg(holder: SendImageHolder, msg: BmobIMMessage, position: Int) {
        val info = msg.bmobIMUserInfo
        val message = BmobIMImageMessage.buildFromDB(true, msg)
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        Glide.with(mContext).load(info?.avatar)
                .error(Glide.with(mContext).load(R.mipmap.default_portrait))
                .into(holder.ivPortrait)
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        Glide.with(mContext).load(if (message.remoteUrl.isEmpty()) message.localPath else message.remoteUrl)
                .error(Glide.with(mContext).load(R.mipmap.iv_load_failure))
                .into(holder.ivContent)
        when(message.sendStatus) {
            BmobIMSendStatus.SEND_FAILED.status, BmobIMSendStatus.UPLOAD_FAILED.status -> {
                holder.ivFailed.visibility = View.VISIBLE
                holder.progress.visibility = View.GONE
                holder.tvStatus.visibility = View.GONE
            }
            BmobIMSendStatus.SENDING.status -> {
                holder.ivFailed.visibility = View.GONE
                holder.progress.visibility = View.VISIBLE
                holder.tvStatus.visibility = View.GONE
            }
            else -> {
                holder.tvStatus.text = "已发送"
                holder.tvStatus.visibility = View.VISIBLE
                holder.ivFailed.visibility = View.GONE
                holder.progress.visibility = View.GONE
            }
        }

        if (mListener != null) {
            holder.ivPortrait.setOnClickListener {
                mListener.onPortraitClick(it, position)
            }
            holder.ivPortrait.setOnLongClickListener {
                mListener.onPortraitLongClick(it, position)
            }
            holder.ivContent.setOnClickListener {
                mListener.onContentClick(it, position)
            }
            holder.ivContent.setOnLongClickListener {
                mListener.onContentLongClick(it, position)
            }
            holder.ivFailed.setOnClickListener {
                mListener.onResendClick(it, holder.tvStatus, holder.progress, position)
            }
        }
    }

    private fun bindReceiveText(holder: ReceiveTextHolder, msg: BmobIMMessage, position: Int) {
        val info = msg.bmobIMUserInfo
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        Glide.with(mContext).load(info?.avatar)
                .error(Glide.with(mContext).load(R.mipmap.default_portrait))
                .into(holder.ivPortrait)
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        holder.tvContent.text = msg.content

        if (mListener != null) {
            holder.ivPortrait.setOnClickListener {
                mListener.onPortraitClick(it, position)
            }
            holder.ivPortrait.setOnLongClickListener {
                mListener.onPortraitLongClick(it, position)
            }
            holder.tvContent.setOnClickListener {
                mListener.onContentClick(it, position)
            }
            holder.tvContent.setOnLongClickListener {
                mListener.onContentLongClick(it, position)
            }
        }
    }

    private fun bindSendText(holder: SendTextHolder, msg: BmobIMMessage, position: Int) {
        val info = msg.bmobIMUserInfo
        holder.tvTime.visibility = if (shouldShowTime(position)) View.VISIBLE else View.GONE
        Glide.with(mContext).load(info?.avatar)
                .error(Glide.with(mContext).load(R.mipmap.default_portrait))
                .into(holder.ivPortrait)
        holder.tvTime.text = TimeUtil.getChatTime(msg.createTime)
        holder.tvContent.text = msg.content

        when(msg.sendStatus) {
            BmobIMSendStatus.SEND_FAILED.status -> {
                holder.ivFailed.visibility = View.VISIBLE
                holder.progress.visibility = View.GONE
            }
            BmobIMSendStatus.SENDING.status -> {
                holder.ivFailed.visibility = View.GONE
                holder.progress.visibility = View.VISIBLE
            }
            else -> {
                holder.ivFailed.visibility = View.GONE
                holder.progress.visibility = View.GONE
            }
        }

        if (mListener != null) {
            holder.ivPortrait.setOnClickListener {
                mListener.onPortraitClick(it, position)
            }
            holder.ivPortrait.setOnLongClickListener {
                mListener.onPortraitLongClick(it, position)
            }
            holder.tvContent.setOnClickListener {
                mListener.onContentClick(it, position)
            }
            holder.tvContent.setOnLongClickListener {
                mListener.onContentLongClick(it, position)
            }
            holder.ivFailed.setOnClickListener {
                mListener.onResendClick(it, holder.tvStatus, holder.progress, position)
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
        lateinit var tvTime: TextView
        lateinit var ivPortrait: ImageView
        lateinit var tvContent: TextView

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
        lateinit var tvTime: TextView
        lateinit var ivPortrait: ImageView
        lateinit var ivContent: ImageView

        init {
            if (itemView != null) {
                tvTime = itemView.tvItemTimeChatReceiveImg
                ivPortrait = itemView.ivItemPortraitChatReceiveImg
                ivContent = itemView.tvItemContentChatReceiveImg
            }
        }
    }

    class SendVoiceHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTime: TextView
        lateinit var ivPortrait: ImageView
        lateinit var ivVoice: ImageView
        lateinit var tvLength: TextView
        lateinit var ivFailed: ImageView
        lateinit var tvStatus: TextView
        lateinit var progress: ProgressBar

        init {
            if (itemView != null) {
                tvTime = itemView.tvItemTimeChatSendVoice
                ivPortrait = itemView.ivItemPortraitChatSendVoice
                ivVoice = itemView.ivItemVoiceChatSendVoice
                tvLength = itemView.tvItemVoiceLengthChatSendVoice
                ivFailed = itemView.ivItemFailedChatSendVoice
                tvStatus = itemView.tvItemStatusChatSendVoice
                progress = itemView.progressItemLoadChanSendVoice
            }
        }
    }

    class ReceiveVoiceHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTime: TextView
        lateinit var ivPortrait: ImageView
        lateinit var ivVoice: ImageView
        lateinit var tvLength: TextView
        lateinit var progrss: ProgressBar

        init {
            if (itemView != null) {
                tvTime = itemView.tvItemTimeChatReceiveVoice
                ivPortrait = itemView.ivItemPortraitChatReceiveVoice
                ivVoice = itemView.ivItemVoiceChatReceiveVoice
                tvLength = itemView.tvItemVoiceLengthChatReceiveVoice
                progrss = itemView.progressItemLoadChanReceiveVoice
            }
        }
    }

    class SendVideoHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
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

    class ReceiveVideoHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTime: TextView
        lateinit var ivPortrait: ImageView
        lateinit var tvContent: TextView

        init {
            if (itemView != null) {
                tvTime = itemView.tvItemTimeChatReceiveText
                ivPortrait = itemView.ivItemPortraitChatReceiveText
                tvContent = itemView.tvItemContentChatReceiveText
            }
        }
    }

    class SendLocationHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTime: TextView
        lateinit var ivPortrait: ImageView
        lateinit var tvContent: TextView
        lateinit var ivFailed: ImageView
        lateinit var tvStatus: TextView
        lateinit var progress: ProgressBar

        init {
            if (itemView != null) {
                tvTime = itemView.tvItemTimeChatSendLocation
                ivPortrait = itemView.ivItemPortraitChatSendLocation
                tvContent = itemView.tvItemLocationChatSendLocation
                ivFailed = itemView.ivItemFailedChatSendLocation
                tvStatus = itemView.tvItemStatusChatSendLocation
                progress = itemView.progressItemLoadChanSendLocation
            }
        }
    }

    class ReceiveLocationHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTime: TextView
        lateinit var ivPortrait: ImageView
        lateinit var tvContent: TextView
        lateinit var progress: ProgressBar

        init {
            if (itemView != null) {
                tvTime = itemView.tvItemTimeChatReceiveLocation
                ivPortrait = itemView.ivItemPortraitChatReceiveLocation
                tvContent = itemView.tvItemLocationChatReceiveLocation
                progress = itemView.progressItemLoadChanReceiveLocation
            }
        }
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