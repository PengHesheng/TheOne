package com.example.a14512.theone.adapter

import android.view.View

/**
 * @author 14512 on 2018/5/5
 */
interface BaseClickListener {
    fun onItemClick(view: View, position: Int)

    fun onItemLongClick(view: View, position: Int) : Boolean
}

interface OnRecyclerItemClickListener: BaseClickListener {

}

interface OnRecyclerChatItemClickListener: BaseClickListener {
    fun onPortraitClick(portrait: View, position: Int)

    fun onPortraitLongClick(view: View, position: Int): Boolean

    fun onResendClick(ivFailed: View, tvStatus: View, progress: View, position: Int)

    fun onContentClick(view: View, position: Int)

    fun onContentLongClick(view: View, position: Int): Boolean
}