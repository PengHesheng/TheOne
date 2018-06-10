package com.example.a14512.theone.view.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.a14512.theone.R
import com.example.a14512.theone.adapter.OnRecyclerItemClickListener
import com.example.a14512.theone.adapter.StudyAdapter
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.model.DateGank
import com.example.a14512.theone.presenter.StudyPresenterImp
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.IStudyView
import kotlinx.android.synthetic.main.activity_study.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @author 14512 on 2018/5/21
 */
class StudyActivity : BaseActivity(), IStudyView {
    private lateinit var mSwipe: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: StudyAdapter

    override fun initView() {
        val toolbar = toolbar
        val ivBack = ivToolbarLeft
        val title = tvToolbarTitle
        mSwipe = studySwipe
        mRecyclerView = studyRecycler

        setSupportActionBar(toolbar)
        title.text = "学一学"
        val presenter = StudyPresenterImp(this, this)
        presenter.getDateGank()
        mAdapter = StudyAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        mRecyclerView.adapter = mAdapter
        mSwipe.setOnRefreshListener { presenter.getDateGank() }
        mAdapter.setOnItemClickListener(object : OnRecyclerItemClickListener{
            override fun onItemClick(view: View, position: Int) {

            }

            override fun onItemLongClick(view: View, position: Int): Boolean {
                return true
            }

            override fun onChildViewClick(view: View, position: Int) {
                WebActivity().actionStart(this@StudyActivity, mAdapter.getData(position))
            }

        })
        ivBack.setOnClickListener { finish() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)
    }

    override fun setAdapter(datas: ArrayList<DateGank.DateGankData>) {
        mAdapter.setDatas(datas)
        mSwipe.isRefreshing = false
    }

    override fun startActivity() {

    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(this, msg)
    }
}
