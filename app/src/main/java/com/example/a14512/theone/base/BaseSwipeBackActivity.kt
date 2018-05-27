package com.example.a14512.theone.base

import android.os.Bundle
import android.support.v4.widget.SlidingPaneLayout
import android.view.View
import android.view.ViewGroup
import com.example.a14512.theone.R

/**
 * @author 14512 on 2018/5/1
 */
abstract class BaseSwipeBackActivity : BaseActivity(), SlidingPaneLayout.PanelSlideListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        initSwipeBackFinish()
        super.onCreate(savedInstanceState)
    }

    private fun initSwipeBackFinish() {
        if (isSupportSwipeBack()) {
            val slidingPaneLayout = SlidingPaneLayout(this)
            //通过反射改变mOverhangSize的值为0，这个mOverhangSize值为菜单到右边屏幕的最短距离，默认
            //是32dp，现在给它改成0
            try {
                //mOverhangSize属性，意思就是左菜单离右边屏幕边缘的距离
                val f_overHang = SlidingPaneLayout::class.java.getDeclaredField("mOverhangSize")
                f_overHang.isAccessible = true
                //设置左菜单离右边屏幕边缘的距离为0，设置全屏
                f_overHang.set(slidingPaneLayout, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            slidingPaneLayout.setPanelSlideListener(this)
            slidingPaneLayout.sliderFadeColor = resources.getColor(android.R.color.transparent)
            // 左侧的透明视图
            val leftView = View(this)
            leftView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            slidingPaneLayout.addView(leftView, 0)  //添加到SlidingPaneLayout中
            // 右侧的内容视图
            val decor = window.decorView as ViewGroup
            val decorChild = decor.getChildAt(0) as ViewGroup
            decorChild.setBackgroundColor(resources.getColor(android.R.color.white))
            decor.removeView(decorChild)
            decor.addView(slidingPaneLayout)
            // 为 SlidingPaneLayout 添加内容视图
            slidingPaneLayout.addView(decorChild, 1)
        }
    }

    /**
     * 是否支持滑动返回
     *
     * @return
     */
    private fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun onPanelClosed(view: View) {

    }

    override fun onPanelOpened(view: View) {
        finish()
        this.overridePendingTransition(0, R.anim.out_to_right)  //右滑动画
    }

    override fun onPanelSlide(view: View, v: Float) {}
}