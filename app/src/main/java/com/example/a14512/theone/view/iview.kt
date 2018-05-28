package com.example.a14512.theone.view

import com.example.a14512.theone.model.Conversation
import com.example.a14512.theone.model.Friend

/**
 * @author 14512 on 2018/5/27
 */
interface IBaseView {
    fun startActivity()

    fun toastMsg(msg: String)
}

interface ILoginView : IBaseView {
    fun getAccount() : String

    fun getPwd() : String

    fun setPortrait(portrait: String?)
}

interface IRegisterView : IBaseView {
    fun getAccount() : String

    fun getPwdAgain() : String

    fun getPwd() : String

    fun setErrorAccount(error: String)

    fun setErrorPwd(error: String)

}

interface IConversationView : IBaseView {
    fun setAdapter(conversations: ArrayList<Conversation>)

    fun addConversations(conversations: ArrayList<Conversation>)
}

interface IContactView : IBaseView {
    fun setAdapter(friends: ArrayList<Friend>)
}

interface ISettingsFragView : IBaseView {
    fun setPortrait(portrait: String)

    fun setName(name: String)

    fun setId(id: String)

    fun setQRCode(QRCode: String)
}

interface IChatView : IBaseView {
    fun setAdapter()
}


interface ISearchView : IBaseView {
    fun getKeyStr() : String
}