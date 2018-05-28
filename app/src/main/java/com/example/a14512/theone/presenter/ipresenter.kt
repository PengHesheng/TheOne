package com.example.a14512.theone.presenter

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
    fun search()
}

interface ISettingsFragPresenter {
    fun getUserInfo()
}