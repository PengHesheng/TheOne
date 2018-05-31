package com.example.a14512.theone.view.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.newim.bean.BmobIMConversation
import cn.bmob.newim.event.MessageEvent
import cn.bmob.newim.event.OfflineMessageEvent
import com.example.a14512.theone.R
import com.example.a14512.theone.adapter.ConversationAdapter
import com.example.a14512.theone.adapter.OnRecyclerItemClickListener
import com.example.a14512.theone.base.BaseFragment
import com.example.a14512.theone.event.RefreshEvent
import com.example.a14512.theone.model.Conversation
import com.example.a14512.theone.presenter.ConversationPresenterImp
import com.example.a14512.theone.presenter.IConversationPresenter
import com.example.a14512.theone.utils.PLog
import com.example.a14512.theone.view.IConversationView
import com.example.a14512.theone.view.activity.ChatActivity
import kotlinx.android.synthetic.main.fragment_conversation.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

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
        mSwipe.isRefreshing = false
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
        mAdapter.setConversations(mConversations)
        mPresenter = ConversationPresenterImp(context!!, this)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = mAdapter

        setClickListener()
    }

    private fun setClickListener() {
        mSwipe.setOnRefreshListener { mPresenter.getConversation() }
        mAdapter.setOnItemClickListener(object : OnRecyclerItemClickListener {
            override fun onChildViewClick(view: View, position: Int) {

            }

            override fun onItemClick(view: View, position: Int) {
                val conversation = mConversations[position] as BmobIMConversation
                ChatActivity().actionStart(context!!, conversation)
            }

            override fun onItemLongClick(view: View, position: Int): Boolean {
                //TODO 长按删除
                return true
            }
        })
    }

    /**注册自定义消息接收事件
     * @param event
     */
    @Subscribe
    fun onEventMainThread(event: RefreshEvent) {
        PLog.e("---会话页接收到自定义消息---")
        //因为新增`新朋友`这种会话类型
        mPresenter.getConversation()
    }

    /**注册离线消息接收事件
     * @param event
     */
    @Subscribe
    fun onEventMainThread(event: OfflineMessageEvent) {
        //重新刷新列表
        mPresenter.getConversation()
    }

    /**注册消息接收事件
     * @param event
     * 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     * 2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    fun onEventMainThread(event: MessageEvent) {
        //重新获取本地消息并刷新列表
        mPresenter.getConversation()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

}