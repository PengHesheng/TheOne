package com.example.a14512.theone.presenter

import android.content.Context
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import com.example.a14512.theone.event.FinishEvent
import com.example.a14512.theone.model.User
import com.example.a14512.theone.model.UserModel
import com.example.a14512.theone.view.IRegisterView
import org.greenrobot.eventbus.EventBus

/**
 * @author 14512 on 2018/5/28
 */
class RegisterPresenterImp(private val mContext: Context, private val mView: IRegisterView) : IRegisterPresenter {

    override fun register() {
        val account = mView.getAccount()
        val pwd = mView.getPwd()
        val pwdAgain = mView.getPwdAgain()
        if (account.isEmpty()) {
            mView.setErrorAccount("用户名不能为空！")
            return
        }
        if (pwd.isEmpty() || pwdAgain.isEmpty()) {
            mView.setErrorPwd("密码不能为空！")
            return
        }
        if (pwd != pwdAgain) {
            mView.setErrorPwd("两次密码不一致！")
            return
        }
        UserModel.getInstance().register(account, pwd, pwdAgain, object : LogInListener<User>() {
            override fun done(p0: User?, p1: BmobException?) {
                if (p1 == null) {
                    EventBus.getDefault().post(FinishEvent())
                    mView.startActivity()
                } else {
                    mView.toastMsg(p1.message + "(" + p1.errorCode + ")")
                }
            }

        })

    }
}