package com.example.a14512.theone.presenter

import android.content.Context
import cn.bmob.newim.BmobIM
import com.example.a14512.theone.model.UserModel
import com.example.a14512.theone.view.ISettingsFragView

/**
 * @author 14512 on 2018/5/28
 */
class SettingsFragPresenterImp(private val mContext: Context,
                               private val mView: ISettingsFragView) : ISettingsFragPresenter {

    override fun getUserInfo() {
        val user = UserModel.getInstance().getCurrentUser()
        if (user != null) {
            mView.setId(user.objectId)
            mView.setName(user.username)
            mView.setPortrait(user.getAvatar())
            mView.setQRCode("")
        }
    }

    override fun loginOut() {
        UserModel.getInstance().logOut()
        BmobIM.getInstance().disConnect()
        mView.toastMsg("退出登录!")
        mView.startActivity()
    }
}