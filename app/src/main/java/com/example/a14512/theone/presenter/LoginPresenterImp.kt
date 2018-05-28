package com.example.a14512.theone.presenter

import android.content.Context
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import com.example.a14512.theone.model.User
import com.example.a14512.theone.model.UserModel
import com.example.a14512.theone.utils.ACache
import com.example.a14512.theone.view.ILoginView

/**
 * @author 14512 on 2018/5/28
 */
class LoginPresenterImp(private val mContext: Context, private val mView: ILoginView) : ILoginPresenter {

    override fun login() {
        val portrait = ACache.getDefault().getAsString("portrait")
        mView.setPortrait(portrait)
        val account = mView.getAccount()
        val pwd = mView.getPwd()
        if (account.isEmpty() || pwd.isEmpty()) {
            mView.toastMsg("账户和密码不能为空！")
        }
        UserModel.getInstance().login(account, pwd, object : LogInListener<User>() {
            override fun done(p0: User?, p1: BmobException?) {
                if (p1 == null && p0 != null) {
                    mView.toastMsg("登录成功")
                    ACache.getDefault().put("portrait", p0.getAvatar())
                    mView.startActivity()

                } else if (p1 != null){
                    mView.toastMsg(p1.message + "(" + p1.errorCode + ")")
                }
            }
        })
    }
}