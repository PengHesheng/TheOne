package com.example.a14512.theone.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a14512.theone.R
import com.example.a14512.theone.model.Conversation

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

    fun deleteConversation(conversation: Conversation) {
        mConversations.remove(conversation)
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    class ConversationViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }
}