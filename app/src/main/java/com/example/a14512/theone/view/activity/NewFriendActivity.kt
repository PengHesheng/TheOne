package com.example.a14512.theone.view.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import com.example.a14512.theone.R
import com.example.a14512.theone.adapter.BaseClickListener
import com.example.a14512.theone.adapter.NewFriendAdapter
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
    private lateinit var mTvNull: TextView

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
        mTvNull = tvNullNewFriend

        setSupportActionBar(toolbar)
        title.text = "新朋友"
        val presenter = NewFriendPresenterImp(this, this)
        mAdapter = NewFriendAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        mRecyclerView.adapter = mAdapter
        presenter.getAllNewFriend()

        ivBack.setOnClickListener {
            finish()
        }
        mSwipe.setOnRefreshListener {
            presenter.getAllNewFriend()
        }
        mAdapter.setOnItemClickListener(object : BaseClickListener {
            override fun onItemClick(view: View, position: Int) {
                val choice = resources.getStringArray(R.array.agree_or_reject)
                val spinner = view.findViewById<Spinner>(R.id.spinnerItemNewFriend)
                val tvHint = view.findViewById<TextView>(R.id.tvItemHintNewFriend)
                spinner.setOnItemClickListener { parent, view, position1, id ->
                    tvHint.text = choice[position1]
                    when(position1) {
                        1 -> {
                            presenter.agreeNewFriend(mAdapter.getNewFriend(position))
                        }

                        2 -> {
                            presenter.rejectNewFriend(mAdapter.getNewFriend(position))
                        }
                    }
                    tvHint.visibility = View.VISIBLE
                    view!!.visibility = View.INVISIBLE
                }
            }

            override fun onItemLongClick(view: View, position: Int): Boolean {
                //删除逻辑
                presenter.deleteNewMsg(mAdapter.getNewFriend(position))
                return true
            }
        })
    }

    override fun startActivity() {

    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(this, msg)
    }

    override fun setAdapter(newFriends: ArrayList<NewFriend>) {
        mAdapter.setNewFriends(newFriends)
        mSwipe.isRefreshing = false
    }

    override fun setViewGone() {
        mTvNull.visibility = View.GONE
    }
}