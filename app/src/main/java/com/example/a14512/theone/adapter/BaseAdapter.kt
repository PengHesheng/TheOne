package com.example.a14512.theone.adapter

import android.support.v7.widget.RecyclerView

/**
 * @author 14512 on 2018/5/8
 */
abstract class BaseAdapter<T : RecyclerView.ViewHolder?> : RecyclerView.Adapter<T>() {

    abstract fun setOnItemClickListener(listener: BaseClickListener)
}