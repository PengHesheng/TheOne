package com.example.a14512.theone.view.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Spinner
import com.example.a14512.theone.R
import com.example.a14512.theone.adapter.NewFriendAdapter
import com.example.a14512.theone.adapter.OnRecyclerItemClickListener
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.model.NewFriend
import com.example.a14512.theone.presenter.NewFriendPresenterImp
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.INewFriendView
import kotlinx.android.synthetic.main.activity_new_friend.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @author 14512 on 2018/6/6
 */
class NewFriendActivity : BaseActivity(), INewFriendView {
    private lateinit var mSwipe: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: NewFriendAdapter
    private var mNewFriends: ArrayList<NewFriend> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_friend)
    }

    override fun initView() {
        val toolbar = toolbar
        val ivBack = ivToolbarLeft
        val title = tvToolbarTitle
        mSwipe = newFriendSwipe
        mRecyclerView = newFriendRecycler

        setSupportActionBar(toolbar)
        title.text = "新朋友"
        val presenter = NewFriendPresenterImp(this, this)
        mAdapter = NewFriendAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = mAdapter
        ivBack.setOnClickListener {
            finish()
        }
        mSwipe.setOnRefreshListener {
            presenter.getAllNewFriend()
        }
        mAdapter.setOnItemClickListener(object : OnRecyclerItemClickListener {
            override fun onItemClick(view: View, position: Int) {

            }

            override fun onItemLongClick(view: View, position: Int): Boolean {
                //删除逻辑
                presenter.deleteNewMsg(mNewFriends[position])
                return true
            }

            override fun onChildViewClick(view: View, position: Int) {
                val spinner = view as Spinner
                spinner.setOnItemClickListener { parent, view, position1, id ->
                    when(position1) {
                        1 -> {
                            presenter.agreeNewFriend(mNewFriends[position])
                        }

                        2 -> {
                            presenter.rejectNewFriend(mNewFriends[position])
                        }
                    }
                }
                //TODO  此处还待优化
            }
        })
    }

    override fun startActivity() {

    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(this, msg)
    }

    override fun setAdapter(newFriends: ArrayList<NewFriend>) {
        mNewFriends = newFriends
        mAdapter.setNewFriends(mNewFriends)
        mSwipe.isRefreshing = false
    }
}