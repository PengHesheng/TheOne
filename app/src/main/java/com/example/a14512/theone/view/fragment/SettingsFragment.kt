package com.example.a14512.theone.view.fragment

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.bmob.newim.bean.BmobIMUserInfo
import com.bumptech.glide.Glide
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseFragment
import com.example.a14512.theone.model.UserModel
import com.example.a14512.theone.presenter.ISettingsFragPresenter
import com.example.a14512.theone.presenter.SettingsFragPresenterImp
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.ISettingsFragView
import com.example.a14512.theone.view.activity.LoginActivity
import com.example.a14512.theone.view.activity.StudyActivity
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
    private lateinit var mLayoutStudy: LinearLayout
    private lateinit var mLayoutSettings: LinearLayout
    private lateinit var mBtnLoginOut: Button
    private lateinit var mPresenter: ISettingsFragPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun initView(view: View) {
        mLayoutUser = layoutUserSettings
        mIvPortrait = ivPortraitSettings
        mTvName = tvNameSettings
        mTvId = tvIdSettings
        mIvQRCode = ivQRCodeSettings
        mLayoutStudy = layoutStudySettings
        mLayoutSettings = layoutSettings
        mBtnLoginOut = btnLoginOut

        mPresenter = SettingsFragPresenterImp(context!!, this)
        mPresenter.getUserInfo()

        mLayoutUser.setOnClickListener {
            val user = UserModel.getInstance().getCurrentUser()
            if (user != null) {
                val info = BmobIMUserInfo(user.objectId, user.username, user.getAvatar())
                UserInfoActivity().actionStart(context!!, info)
            }
        }
        mLayoutStudy.setOnClickListener {
            startActivity(context!!, StudyActivity::class.java)
        }
        btnLoginOut.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context!!)
                    .setTitle("提示")
                    .setMessage("确定要退出吗？")
                    .setNegativeButton("取消", {dialog, which -> dialog.dismiss() })
                    .setPositiveButton("确定") { dialog, which -> mPresenter.loginOut() }
                    .create()
            alertDialog.show()

        }
    }

    override fun startActivity() {
        startActivity(context!!, LoginActivity::class.java)
        activity!!.finish()
    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(context!!, msg)
    }

    override fun setPortrait(portrait: String?) {
        Glide.with(context!!).load(portrait ?: "")
                .error(Glide.with(context!!).load(R.mipmap.default_portrait))
                .into(mIvPortrait)
    }

    override fun setName(name: String) {
        mTvName.text = name
    }

    override fun setId(id: String) {
        mTvId.text = id
    }

    override fun setQRCode(QRCode: String) {
        Glide.with(context!!).load(QRCode).into(mIvQRCode)
    }

}