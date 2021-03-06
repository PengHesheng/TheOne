package com.example.a14512.theone.view.activity

import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.model.UserModel
import kotlinx.android.synthetic.main.activity_welcome.*

/**
 * @author 14512 on 2018/5/28
 */
class WelcomeActivity : BaseActivity(){
    override fun initView() {
        val ivBg = ivWelcome
        Glide.with(this).load(R.drawable.launcher_bg).into(ivBg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val handler = Handler(mainLooper)
        handler.postDelayed({
            val user = UserModel.getInstance().getCurrentUser()
            if (user == null) {
                startActivity(this, LoginActivity::class.java)
                finish()
            } else {
                startActivity(this, MainActivity::class.java)
                finish()
            }
        }, 1000)
    }
}