package com.example.a14512.theone.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.a14512.theone.R
import com.example.a14512.theone.model.Friend
import kotlinx.android.synthetic.main.item_contact_recycler.view.*

/**
 * @author 14512 on 2018/6/4
 */
class ContactAdapter : BaseAdapter<RecyclerView.ViewHolder>() {
    private lateinit var mContext: Context
    private lateinit var mListener: BaseClickListener
    private var mFriends: ArrayList<Friend> = ArrayList()

    fun setFriends(friends: ArrayList<Friend>) {
        mFriends = friends
        notifyDataSetChanged()
    }

    fun deleteFriend(position: Int) {
        mFriends.removeAt(position)
        notifyDataSetChanged()
    }

    fun deleteFriend(friend: Friend) {
        mFriends.remove(friend)
        notifyDataSetChanged()
    }

    fun addFriend(friend: Friend) {
        mFriends.add(friend)
        notifyDataSetChanged()
    }

    override fun setOnItemClickListener(listener: BaseClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return ContactHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_contact_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return mFriends.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactHolder) {
            val friend = mFriends[position]
            val friendUser = friend.getFriendUser()
            Glide.with(mContext).load(friendUser.getAvatar())
                    .error(R.mipmap.ic_launcher_round).into(holder.ivPortrait)
            holder.tvName.text = friendUser.username
            if (mListener != null) {
                holder.itemView.setOnClickListener {
                    mListener.onItemClick(it, position)
                }
                holder.itemView.setOnClickListener {
                    mListener.onItemLongClick(it, position)
                }
            }
        }
    }

    class ContactHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var ivPortrait: ImageView
        lateinit var tvName: TextView

        init {
            if (itemView != null) {
                ivPortrait = itemView.ivItemPortraitContact
                tvName = itemView.tvItemNameContact
            }
        }
    }
}