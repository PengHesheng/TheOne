package com.example.a14512.theone.view.activity

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.widget.GridView
import android.widget.ImageView
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.model.BaseModel
import com.example.a14512.theone.model.User
import com.example.a14512.theone.model.UserModel
import com.example.a14512.theone.utils.PLog
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.include_search_toolbar.*

/**
 * @author 14512 on 2018/5/21
 */
class SearchActivity : BaseActivity() {
    private lateinit var mGridView: GridView
    private lateinit var mSearchView: SearchView
    private lateinit var mIvBack: ImageView

    override fun initView() {
        val toolbar = searchToolbar
        mIvBack = ivSearchBack
        mSearchView = searchAutoComp
        mGridView = searchGrid

        setSupportActionBar(toolbar)
        mIvBack.setOnClickListener { finish() }
        mSearchView.setIconifiedByDefault(false)
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    UserModel.getInstance().queryUsers(query, BaseModel.DEFAULT_LIMIT,
                            object : FindListener<User>() {
                                override fun done(p0: MutableList<User>?, p1: BmobException?) {
                                    PLog.e("Search result", p0?.size.toString() + "\t" + p1.toString())
                                }
                            })
                }
                return true
            }
        })


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }
}
