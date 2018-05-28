package com.example.a14512.theone.view.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.utils.KeyBoradUtil
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.include_chat_bottom_key_broad.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @author 14512 on 2018/5/21
 */
class ChatActivity : BaseActivity() {
    private lateinit var mLayoutChat: LinearLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSwipe: SwipeRefreshLayout
    private lateinit var mIvBack: ImageView
    private lateinit var mIBtnVoice: ImageButton
    private lateinit var mBtnSpeak: Button
    private lateinit var mEtContent: EditText
    private lateinit var mBtnFace: Button
    private lateinit var mBtnAdd: Button
    private lateinit var mLayoutBottom: LinearLayout
    private lateinit var mViewPager: ViewPager
    private var isVoiceClicked: Boolean = true
    private var isFaceClicked: Boolean = true
    private var isAddClicked: Boolean = true


    override fun initView() {
        mLayoutChat = layoutChat
        mRecyclerView = chatRecycler
        mSwipe = chatSwipe
        val toolbar = toolbar
        var title = tvToolbarTitle
        mIvBack = ivToolbarLeft
        mIBtnVoice = btnChatVoice
        mBtnSpeak = btnChatSpeak
        mEtContent = etChat
        mBtnFace = btnChatLaughFace
        mBtnAdd = btnChatAdd
        mLayoutBottom = layoutChatBottom
        mViewPager = viewPagerChat

        setSupportActionBar(toolbar)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setOnClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListener() {
        mLayoutChat.setOnClickListener { KeyBoradUtil.hideInputFromWindow(this, mLayoutChat) }
        mIvBack.setOnClickListener { finish() }
        mIBtnVoice.setOnClickListener {
            if (isVoiceClicked) {
                mIBtnVoice.setImageResource(R.mipmap.key_borad)
                mEtContent.visibility = View.GONE
                mBtnSpeak.visibility = View.VISIBLE
                isVoiceClicked = false
            } else {
                mIBtnVoice.setImageResource(android.R.drawable.ic_btn_speak_now)
                mEtContent.visibility = View.VISIBLE
                mEtContent.focusable = View.FOCUSABLE
                mBtnSpeak.visibility = View.GONE
                isVoiceClicked = true
            }
        }
        mBtnFace.setOnClickListener {
            if (isFaceClicked) {
                if (mLayoutBottom.visibility != View.VISIBLE) {
                    mLayoutBottom.visibility = View.VISIBLE
                }
                KeyBoradUtil.hideSoftInputFromWindow(this, mEtContent)
                isFaceClicked = false
            } else {
                mLayoutBottom.visibility = View.GONE
                mEtContent.focusable = View.FOCUSABLE
                isFaceClicked = true
            }
        }
        mBtnAdd.setOnClickListener {
            if (isAddClicked) {
                if (mLayoutBottom.visibility != View.VISIBLE) {
                    mLayoutBottom.visibility = View.VISIBLE
                }
                KeyBoradUtil.hideSoftInputFromWindow(this, mEtContent)
                isAddClicked = false
            } else {
                mLayoutBottom.visibility = View.GONE
                mEtContent.focusable = View.FOCUSABLE
                isAddClicked = true
            }
        }
        mEtContent.setOnClickListener {
            KeyBoradUtil.showSoftInputFromWindow(this, mEtContent)
            if (mLayoutBottom.visibility == View.VISIBLE) {
                mLayoutBottom.visibility = View.GONE
            }
        }

    }
}
