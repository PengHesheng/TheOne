package com.example.a14512.theone.view.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a14512.theone.R
import com.example.a14512.theone.adapter.ConversationAdapter
import com.example.a14512.theone.base.BaseFragment
import com.example.a14512.theone.model.Conversation
import com.example.a14512.theone.presenter.ConversationPresenterImp
import com.example.a14512.theone.presenter.IConversationPresenter
import com.example.a14512.theone.view.IConversationView
import kotlinx.android.synthetic.main.fragment_conversation.*

/**
 * @author 14512 on 2018/5/21
 */
class ConversationFragment: BaseFragment(), IConversationView {

    private lateinit var mSwipe: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ConversationAdapter
    private lateinit var mPresenter: IConversationPresenter
    private var mConversations: ArrayList<Conversation> = ArrayList()

    override fun startActivity() {

    }

    override fun toastMsg(msg: String) {
    }

    override fun setAdapter(conversations: ArrayList<Conversation>) {
        mConversations = conversations
    }

    override fun addConversations(conversations: ArrayList<Conversation>) {
        mAdapter.addConversations(conversations)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_conversation, container, false)
    }

    override fun initView(view: View) {
        mSwipe = conversationSwipe
        mRecyclerView = conversationRecycler

        mAdapter = ConversationAdapter()
        mAdapter.addConversations(mConversations)
        mPresenter = ConversationPresenterImp(context!!, this)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = mAdapter
    }
}