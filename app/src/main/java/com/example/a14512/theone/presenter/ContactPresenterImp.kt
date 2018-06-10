package com.example.a14512.theone.presenter

import android.content.Context
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.example.a14512.theone.model.Friend
import com.example.a14512.theone.model.UserModel
import com.example.a14512.theone.view.IContactView

/**
 * @author 14512 on 2018/6/4
 */
class ContactPresenterImp(private val mContext: Context,
                          private val mView: IContactView) : IContactPresenter {

    override fun getContact() {
        UserModel.getInstance().queryFriends(object : FindListener<Friend>() {
            override fun done(p0: MutableList<Friend>?, p1: BmobException?) {
                if (p1 == null) {
                    if (p0 != null) {
                        mView.setAdapter(p0 as ArrayList<Friend>)
                    } else {
                        mView.setAdapter(ArrayList())
                    }
                } else {
                    mView.toastMsg(p1.message.toString())
                    mView.setAdapter(ArrayList())
                }
            }
        })
    }
}