package com.example.a14512.theone.presenter

import android.content.Context
import com.example.a14512.theone.model.UserModel
import com.example.a14512.theone.view.ISettingsFragView

/**
 * @author 14512 on 2018/5/28
 */
class SettingsFragPresenterImp(private val mContext: Context,
                               private val mView: ISettingsFragView) : ISettingsFragPresenter {

    override fun getUserInfo() {
        val user = UserModel.getInstance().getCurrentUser()
        mView.setId(user.objectId)
        mView.setName(user.username)
        mView.setPortrait(user.getAvatar()!!)
        mView.setQRCode("")
    }
}