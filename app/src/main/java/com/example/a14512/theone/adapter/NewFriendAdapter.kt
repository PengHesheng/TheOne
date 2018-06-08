package com.example.a14512.theone.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.a14512.theone.R
import com.example.a14512.theone.STATUS_VERIFY_NONE
import com.example.a14512.theone.STATUS_VERIFY_READED
import com.example.a14512.theone.model.NewFriend
import kotlinx.android.synthetic.main.item_new_friend_recycler.view.*

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
        return NewFriendHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_new_friend_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return mNewFriends.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewFriendHolder) {
            val friend = mNewFriends[position]
            Glide.with(mContext).load(friend.avatar)
                    .error(R.mipmap.ic_launcher_round).into(holder.ivPortrait)
            holder.tvName.text = friend.name
            val status = friend.status
            if (status == null || status == STATUS_VERIFY_NONE
                    || status == STATUS_VERIFY_READED || mListener != null) {
                //TODO view的暴露有待优化
                holder.spinner.setOnClickListener {
                    val choice = mContext.resources.getStringArray(R.array.agree_or_reject)
                    holder.tvHint.text = choice[position]
                    mListener.onChildViewClick(it, position)
                    it.visibility = View.GONE
                    holder.tvHint.visibility = View.VISIBLE
                }
                holder.itemView.setOnLongClickListener {
                    mListener.onItemLongClick(it, position)
                }
            } else {
                holder.tvHint.text =  "已添加"
                holder.tvHint.visibility = View.VISIBLE
                holder.spinner.visibility = View.GONE
            }
        }
    }

    class NewFriendHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var ivPortrait: ImageView
        lateinit var tvName: TextView
        lateinit var tvHint: TextView
        lateinit var spinner: Spinner

        init {
            if (itemView != null) {
                ivPortrait = itemView.ivItemPortraitNewFriend
                tvName = itemView.tvItemNameNewFriend
                tvHint = itemView.tvItemHintNewFriend
                spinner = itemView.spinnerItemNewFriend
            }
        }
    }
}