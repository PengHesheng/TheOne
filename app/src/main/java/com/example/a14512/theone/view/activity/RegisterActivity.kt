package com.example.a14512.theone.view.activity

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.presenter.IRegisterPresenter
import com.example.a14512.theone.presenter.RegisterPresenterImp
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.IRegisterView
import kotlinx.android.synthetic.main.activity_register.*

/**
 * @author 14512 on 2018/5/28
 */
class RegisterActivity : BaseActivity(), IRegisterView {

    private lateinit var mInputAccount: TextInputLayout
    private lateinit var mInputPwd: TextInputLayout
    private lateinit var mInputPwdAgain: TextInputLayout
    private lateinit var mEtAccount: TextInputEditText
    private lateinit var mEtPwd: TextInputEditText
    private lateinit var mEtPwdAgain: TextInputEditText

    override fun startActivity() {
        startActivity(this, MainActivity::class.java)
        finish()
        //TODO 还应结束login，需要写一个活动管理类
    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(this, msg)
    }

    override fun getAccount(): String {
        return mEtAccount.text.toString()
    }

    override fun getPwdAgain(): String {
        return mEtPwdAgain.text.toString()
    }

    override fun getPwd(): String {
        return mEtPwd.text.toString()
    }

    override fun setErrorAccount(error: String) {
        mInputAccount.error = error
    }

    override fun setErrorPwd(error: String) {
        mInputPwd.error = error
        mInputPwdAgain.error = error
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun initView() {
        mInputAccount = textInputLayoutAccountRegister
        mInputPwd = textInputLayoutPwdRegister
        mInputPwdAgain = textInputLayoutPwdAgainRegister
        mEtAccount = etAccountRegister
        mEtPwd = etPwdRegister
        mEtPwdAgain = etPwdAgainRegister
        val btnRegister = btnRegister
        val presenter: IRegisterPresenter = RegisterPresenterImp(this, this)
        btnRegister.setOnClickListener { presenter.register() }

    }
}