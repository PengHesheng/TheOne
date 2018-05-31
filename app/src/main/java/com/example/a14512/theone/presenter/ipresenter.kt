package com.example.a14512.theone.presenter

import cn.bmob.newim.bean.BmobIMUserInfo

/**
 * @author 14512 on 2018/5/27
 */
interface ILoginPresenter {
    fun login()
}

interface IRegisterPresenter {
    fun register()
}

interface IConversationPresenter {
    fun getConversation()
}

interface IContactPresenter {
    fun getContact()
}

interface IChatPresenter {
    fun getMsgs()
}

interface ISearchPresenter {
    fun search(keyString: String)

    fun sendAddFriendMsg(info: BmobIMUserInfo)
}

interface ISettingsFragPresenter {
    fun getUserInfo()
}

interface IUserInfoPresenter {
    fun sendAddFriendMsg(info: BmobIMUserInfo)
}