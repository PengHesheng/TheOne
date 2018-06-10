package com.example.a14512.theone.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.model.DateGank
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @author 14512 on 2018/6/9
 */
class WebActivity : BaseActivity() {

    fun actionStart(context: Context, data: DateGank.DateGankData) {
        context.startActivity(Intent(context, WebActivity::class.java).putExtra("data", data))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
    }

    override fun initView() {
        val toolbar = toolbar
        val title = tvToolbarTitle
        val ivBack = ivToolbarLeft
        val webView = webView

        setSupportActionBar(toolbar)
        val data = intent.getSerializableExtra("data") as DateGank.DateGankData
        if (data != null) {
            title.text = data.type
            webView.loadUrl(data.url)
        }
        ivBack.setOnClickListener { finish() }
    }
}