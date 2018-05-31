package com.example.a14512.theone.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import cn.bmob.newim.bean.BmobIMUserInfo
import com.bumptech.glide.Glide
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseFragment
import com.example.a14512.theone.model.UserModel
import com.example.a14512.theone.presenter.ISettingsFragPresenter
import com.example.a14512.theone.presenter.SettingsFragPresenterImp
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.ISettingsFragView
import com.example.a14512.theone.view.activity.UserInfoActivity
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * @author 14512 on 2018/5/21
 */
class SettingsFragment: BaseFragment(), ISettingsFragView {

    private lateinit var mLayoutUser: RelativeLayout
    private lateinit var mIvPortrait: ImageView
    private lateinit var mTvName: TextView
    private lateinit var mTvId: TextView
    private lateinit var mIvQRCode: ImageView
    private lateinit var mLayoutSettings: LinearLayout
    private lateinit var mPresenter: ISettingsFragPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        return view
    }

    override fun initView(view: View) {
        mLayoutUser = layoutUserSettings
        mIvPortrait = ivPortraitSettings
        mTvName = tvNameSettings
        mTvId = tvIdSettings
        mIvQRCode = ivQRCodeSettings
        mLayoutSettings = layoutSettings

        mPresenter = SettingsFragPresenterImp(context!!, this)
        mPresenter.getUserInfo()

        mLayoutUser.setOnClickListener {
            val user = UserModel.getInstance().getCurrentUser()
            val info = BmobIMUserInfo(user.objectId, user.username, user.getAvatar())
            UserInfoActivity().actionStart(context!!, info)
        }
    }

    override fun startActivity() {

    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(context!!, msg)
    }

    override fun setPortrait(portrait: String?) {
        Glide.with(context).load(portrait ?: "").error(R.mipmap.ic_launcher_round).into(mIvPortrait)
    }

    override fun setName(name: String) {
        mTvName.text = name
    }

    override fun setId(id: String) {
        mTvId.text = id
    }

    override fun setQRCode(QRCode: String) {
        Glide.with(context).load(QRCode).into(mIvQRCode)
    }

}