package com.example.a14512.theone.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMUserInfo
import com.bumptech.glide.Glide
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.model.UserModel
import com.example.a14512.theone.presenter.UserInfoPresenterImp
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.IUserInfoView
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @author 14512 on 2018/5/21
 */
class UserInfoActivity : BaseActivity(), IUserInfoView {
    private lateinit var mUserInfo: BmobIMUserInfo

    fun actionStart(context: Context, userInfo: BmobIMUserInfo) {
        context.startActivity(Intent(context, UserInfoActivity::class.java)
                .putExtra("userInfo", userInfo))
    }

    override fun initView() {
        val toolbar = toolbar
        val ivBack = ivToolbarLeft
        val title = tvToolbarTitle
        val ivPortrait = ivPortraitUserInfo
        val name = tvAccountUserInfo
        val btnAdd = btnAddUserInfo
        val btnChat = btnChatUserInfo

        setSupportActionBar(toolbar)
        title.text = "个人资料"
        val presenter = UserInfoPresenterImp(this, this)
        mUserInfo = intent.getSerializableExtra("userInfo") as BmobIMUserInfo
        if (mUserInfo != null) {
            if (mUserInfo.userId == UserModel.getInstance().getCurrentUser().objectId) {
                btnAdd.visibility = View.GONE
                btnChat.visibility = View.GONE
            } else {
                btnAdd.visibility = View.VISIBLE
                btnChat.visibility = View.VISIBLE
            }
            Glide.with(this).load(mUserInfo.avatar).error(R.mipmap.ic_launcher_round).into(ivPortrait)
            name.text = mUserInfo.name
        }
        btnAdd.setOnClickListener {
            presenter.sendAddFriendMsg(mUserInfo)
        }
        btnChat.setOnClickListener {
            val conversationEntrance = BmobIM.getInstance().startPrivateConversation(mUserInfo, null)
            ChatActivity().actionStart(this, conversationEntrance)
        }
        ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
    }

    override fun startActivity() {
    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(this, msg)
    }
}
