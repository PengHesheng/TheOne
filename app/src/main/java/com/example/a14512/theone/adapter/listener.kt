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