package com.example.a14512.theone.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.readystatesoftware.systembartint.SystemBarTintManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * @author 14512 on 2018/5/1
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initView()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        initView()
    }

    protected abstract fun initView()

    fun setStatusBarColor(color: Int) {
        /**
         * Android4.4以上可用
         * 将状态栏颜色改变
         * 沉浸式请看郭霖的：http://blog.csdn.net/guolin_blog/article/details/51763825
         * 即 onWindowFocusChanged（）
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//状态栏
            val tintManager = SystemBarTintManager(this)
            tintManager.setStatusBarTintResource(color)
            tintManager.isStatusBarTintEnabled = true
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe
    fun onEvent(empty: Boolean) {}

    fun startActivity(context: Context, target: Class<out Activity>) {
        startActivity(Intent().setClass(context, target))
    }

}