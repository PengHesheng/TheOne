package com.example.a14512.theone.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.a14512.theone.R
import com.example.a14512.theone.model.User
import com.example.a14512.theone.utils.GlideUtil
import kotlinx.android.synthetic.main.item_search_user_recycler.view.*

/**
 * @author 14512 on 2018/5/31
 */
class SearchUserAdapter : BaseAdapter<RecyclerView.ViewHolder>() {
    private lateinit var mContext: Context
    private lateinit var mListener: OnRecyclerItemClickListener
    private var mUsers: ArrayList<User> = ArrayList()

    fun setUsers(users: ArrayList<User>) {
        mUsers = users
        notifyDataSetChanged()
    }

    fun getUser(position: Int): User {
        return mUsers[position]
    }

    override fun setOnItemClickListener(listener: BaseClickListener) {
        mListener = listener as OnRecyclerItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return SearchUserHolder(LayoutInflater.from(mContext).inflate(R.layout.item_search_user_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchUserHolder) {
            val user = mUsers[position]
            GlideUtil.glidePortrait(mContext, user.getAvatar(), holder.ivPortrait)
            holder.tvName.text = user.username
            if (mListener != null) {
                holder.btnAdd.setOnClickListener {
                    mListener.onChildViewClick(it, position)
                }
                holder.itemView.setOnClickListener {
                    mListener.onItemClick(it, position)
                }
                holder.itemView.setOnLongClickListener {
                    mListener.onItemLongClick(it, position)
                }
            }
        }
    }

    class SearchUserHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var ivPortrait: ImageView
        lateinit var tvName: TextView
        lateinit var btnAdd: Button

        init {
            if (itemView != null) {
                ivPortrait = itemView.ivPortraitSearch
                tvName = itemView.tvNameSearch
                btnAdd = itemView.btnAddSearch
            }
        }
    }
}