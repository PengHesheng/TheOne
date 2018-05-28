package com.example.a14512.theone.view.activity

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputEditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.presenter.LoginPresenterImp
import com.example.a14512.theone.utils.KeyBoradUtil
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.ILoginView
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author 14512 on 2018/5/28
 */
class LoginActivity : BaseActivity(), ILoginView {

    private lateinit var mLayout: ConstraintLayout
    private lateinit var mIvPortrait: ImageView
    private lateinit var mEtAccount: TextInputEditText
    private lateinit var mEtPwd: TextInputEditText

    override fun startActivity() {
        startActivity(this, MainActivity::class.java)
        finish()
    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(this, msg)
    }

    override fun getAccount(): String {
        return mEtAccount.text.toString()
    }

    override fun getPwd(): String {
        return  mEtPwd.text.toString()
    }

    override fun setPortrait(portrait: String?) {
        Glide.with(this).load(portrait).error(R.mipmap.ic_launcher_round).into(mIvPortrait)
    }

    override fun initView() {
        mLayout = layoutLogin
        mIvPortrait = ivPortraitLogin
        mEtAccount = etAccountLogin
        mEtPwd = etPwdLogin
        val btnLogin = btnLogin

        mLayout.setOnClickListener { KeyBoradUtil.hideInputFromWindow(this, mLayout) }
        val presenter = LoginPresenterImp(this, this)
        btnLogin.setOnClickListener { presenter.login() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}