package com.example.a14512.theone.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import cn.bmob.newim.bean.BmobIMUserInfo
import com.example.a14512.theone.R
import com.example.a14512.theone.adapter.OnRecyclerItemClickListener
import com.example.a14512.theone.adapter.SearchUserAdapter
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.model.User
import com.example.a14512.theone.presenter.ISearchPresenter
import com.example.a14512.theone.presenter.SearchPresenterImp
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.ISearchView
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.include_search_toolbar.*

/**
 * @author 14512 on 2018/5/21
 */
class SearchActivity : BaseActivity(), ISearchView {
    private lateinit var mGridView: GridView
    private lateinit var mSearchView: SearchView
    private lateinit var mIvBack: ImageView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mTvNoData: TextView
    private lateinit var mAdapter: SearchUserAdapter
    private lateinit var mPresenter: ISearchPresenter
    private var mUsers: ArrayList<User> = ArrayList()

    override fun initView() {
        val toolbar = searchToolbar
        mIvBack = ivSearchBack
        mSearchView = searchAutoComp
        mGridView = searchGrid
        mRecyclerView = searchResultRecycler
        mTvNoData = tvNoDataSearch

        setSupportActionBar(toolbar)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = layoutManager
        mAdapter = SearchUserAdapter()
        mRecyclerView.adapter = mAdapter
        mPresenter = SearchPresenterImp(this, this)
        mIvBack.setOnClickListener { finish() }
        mSearchView.setIconifiedByDefault(false)
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mPresenter.search(query)
                } else {
                    toastMsg("搜索不能为空！")
                }
                return true
            }
        })
        mAdapter.setOnItemClickListener(object : OnRecyclerItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val user = mUsers[position]
                val userInfo = BmobIMUserInfo(user.objectId, user.username, user.getAvatar())
                UserInfoActivity().actionStart(this@SearchActivity, userInfo)
            }

            override fun onItemLongClick(view: View, position: Int): Boolean {
                return false
            }

            override fun onChildViewClick(view: View, position: Int) {
                val user = mUsers[position]
                val userInfo = BmobIMUserInfo(user.objectId, user.username, user.getAvatar())
                mPresenter.sendAddFriendMsg(userInfo)
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    override fun setAdapter(users: ArrayList<User>) {
        if (users.isNotEmpty()) {
            mTvNoData.visibility = View.GONE
            mGridView.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
            mUsers = users
            mAdapter.setUsers(mUsers)
        } else {
            mTvNoData.visibility = View.VISIBLE
            mTvNoData.text = "找不到结果"
            mRecyclerView.visibility = View.GONE
            mGridView.visibility = View.GONE
        }
    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(this, msg)
    }

    override fun startActivity() {
    }
}
