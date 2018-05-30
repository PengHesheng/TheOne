package com.example.a14512.theone.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.bmob.newim.bean.BmobIMUserInfo
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseActivity

/**
 * @author 14512 on 2018/5/21
 */
class UserInfoActivity : BaseActivity() {

    fun actionStart(context: Context, userInfo: BmobIMUserInfo) {
        context.startActivity(Intent(context, UserInfoActivity::class.java)
                .putExtra("userInfo", userInfo))
    }

    override fun initView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
    }
}
