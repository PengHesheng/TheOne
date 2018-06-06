package com.example.a14512.theone.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.a14512.theone.model.NewFriend

/**
 * @author 14512 on 2018/6/6
 */
class NewFriendAdapter : BaseAdapter<RecyclerView.ViewHolder>() {
    private lateinit var mContext: Context
    private lateinit var mListener: OnRecyclerItemClickListener
    private var mNewFriends: ArrayList<NewFriend> = ArrayList()

    fun setNewFriends(newFriends: ArrayList<NewFriend>) {
        mNewFriends = newFriends
        notifyDataSetChanged()
    }

    fun addNewFriend(newFriend: NewFriend) {
        mNewFriends.add(newFriend)
        notifyDataSetChanged()
    }

    fun deleteNewFriend(newFriend: NewFriend) {
        mNewFriends.remove(newFriend)
        notifyDataSetChanged()
    }

    fun deleteNewFriend(position: Int) {
        mNewFriends.removeAt(position)
        notifyDataSetChanged()
    }

    override fun setOnItemClickListener(listener: BaseClickListener) {
        mListener = listener as OnRecyclerItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return NewFriendHolder(null)
    }

    override fun getItemCount(): Int {
        return mNewFriends.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    class NewFriendHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}