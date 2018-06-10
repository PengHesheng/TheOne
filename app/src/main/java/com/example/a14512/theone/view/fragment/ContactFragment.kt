package com.example.a14512.theone.view.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMUserInfo
import com.example.a14512.theone.R
import com.example.a14512.theone.adapter.BaseClickListener
import com.example.a14512.theone.adapter.ContactAdapter
import com.example.a14512.theone.base.BaseFragment
import com.example.a14512.theone.model.Friend
import com.example.a14512.theone.presenter.ContactPresenterImp
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.IContactView
import com.example.a14512.theone.view.activity.ChatActivity
import kotlinx.android.synthetic.main.fragment_contact.*

/**
* @author 14512 on 2018/5/21
*/
class ContactFragment: BaseFragment(), IContactView {
    private lateinit var mSwipe: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ContactAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun initView(view: View) {
        mSwipe = contactSwipe
        mRecyclerView = contactRecycler

        val presenter = ContactPresenterImp(context!!, this)
        presenter.getContact()
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        mAdapter = ContactAdapter()
        mRecyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : BaseClickListener {
            override fun onItemClick(view: View, position: Int) {
                val friend = mAdapter.getFriend(position)
                val friendUser = friend.getFriendUser()
                val info = BmobIMUserInfo(friendUser.objectId, friendUser.username, friendUser.getAvatar())
                val conversation = BmobIM.getInstance().startPrivateConversation(info, null)
                ChatActivity().actionStart(context!!, conversation)
            }

            override fun onItemLongClick(view: View, position: Int): Boolean {
                return true
            }

        })
        mSwipe.setOnRefreshListener {
            presenter.getContact()
        }
    }

    override fun startActivity() {

    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(context!!, msg)
    }

    override fun setAdapter(friends: ArrayList<Friend>) {
        mAdapter.setFriends(friends)
        mSwipe.isRefreshing = false
    }
}