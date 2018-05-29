package com.example.a14512.theone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMConversation
import cn.bmob.newim.bean.BmobIMMessageType
import com.bumptech.glide.Glide
import com.example.a14512.theone.R
import com.example.a14512.theone.model.Conversation
import com.example.a14512.theone.utils.TimeUtil
import kotlinx.android.synthetic.main.item_conversation_recycler.view.*

/**
 * @author 14512 on 2018/5/28
 */
class ConversationAdapter() : BaseAdapter<RecyclerView.ViewHolder>() {
    private lateinit var mContext: Context
    private lateinit var mListener: OnRecyclerItemClickListener
    private var mConversations: ArrayList<Conversation> = ArrayList()

    fun setConversations(conversations: ArrayList<Conversation>) {
        mConversations = conversations
        notifyDataSetChanged()
    }

    fun addConversations(conversations: ArrayList<Conversation>) {
        mConversations.addAll(conversations)
        notifyDataSetChanged()
    }

    fun deleteConversation(position: Int) {
        mConversations.removeAt(position)
        notifyDataSetChanged()
    }

    override fun setOnItemClickListener(listener: BaseClickListener) {
        mListener = listener as OnRecyclerItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_conversation_recycler, parent, false)
        return ConversationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mConversations.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ConversationViewHolder) {
            val conversation = mConversations[position] as BmobIMConversation
            val msgs = conversation.messages
            if (msgs != null && msgs.isNotEmpty()) {
                val lastMsg = msgs[0]
                val content = lastMsg.content
                when (lastMsg.msgType) {
                    BmobIMMessageType.TEXT.type -> holder.tvContent.text = content
                    BmobIMMessageType.IMAGE.type -> holder.tvContent.text = "[图片]"
                    BmobIMMessageType.VOICE.type -> holder.tvContent.text = "[语音]"
                    BmobIMMessageType.LOCATION.type -> holder.tvContent.text = "[位置]$content"
                    else -> holder.tvContent.text = "[未知]" //开发者自定义的消息类型，需要自行处理
                }
                holder.tvTime.text = TimeUtil.getChatTime(lastMsg.createTime)
            }
            Glide.with(mContext).load(conversation.conversationIcon).
                    error(R.mipmap.ic_launcher_round).into(holder.ivPortrait)
            holder.tvName.text = conversation.conversationTitle
            val unRead = BmobIM.getInstance().getUnReadCount(conversation.conversationId)
            if (unRead > 0) {
                holder.tvUnread.visibility = View.VISIBLE
                holder.tvUnread.text = unRead.toString()
            } else {
                holder.tvUnread.visibility = View.GONE
            }
            holder.itemView.setOnClickListener {
                mListener.onItemClick(it, position)
            }
            holder.itemView.setOnLongClickListener {
                mListener.onItemLongClick(it, position)
            }
            //TODO 未读消息滑动标记为已读
        }
    }

    class ConversationViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        lateinit var ivPortrait: ImageView
        lateinit var tvName: TextView
        lateinit var tvContent: TextView
        lateinit var tvUnread: TextView
        lateinit var tvTime: TextView

        init {
            if (itemView != null) {
                ivPortrait = itemView.ivItemPortraitConversation
                tvName = itemView.tvItemUserNameConversation
                tvContent = itemView.tvItemFirstMsgConversation
                tvUnread = itemView.tvItemUnreadConversation
                tvTime = itemView.tvItemTimeConversation
            }
        }

    }
}