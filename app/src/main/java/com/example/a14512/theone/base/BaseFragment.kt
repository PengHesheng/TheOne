package com.example.a14512.theone.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

/**
 * @author 14512 on 2018/5/3
 */
abstract class BaseFragment: Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    protected abstract fun initView(view: View)

    fun startActivity(context: Context, target: Class<out Activity>) {
        startActivity(Intent().setClass(context, target))
    }

    fun startActivity(context: Context, target: Class<out Activity>, bundle: Bundle, key: String) {
        startActivity(Intent().putExtra(key, bundle).setClass(context, target))
    }

}