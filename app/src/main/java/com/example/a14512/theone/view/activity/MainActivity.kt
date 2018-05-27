package com.example.a14512.theone.view.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.PopupMenu
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMUserInfo
import cn.bmob.newim.core.ConnectionStatus
import cn.bmob.newim.listener.ConnectListener
import cn.bmob.newim.listener.ConnectStatusChangeListener
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.event.RefreshEvent
import com.example.a14512.theone.model.User
import com.example.a14512.theone.utils.PLog
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.fragment.ContactFragment
import com.example.a14512.theone.view.fragment.ConversationFragment
import com.example.a14512.theone.view.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity() {
    private lateinit var mToolbar: Toolbar
    private lateinit var mIvAdd: ImageView
    private lateinit var mIvSearch: ImageView
    private lateinit var mBottomNav: BottomNavigationView
    private var mConversationFrag: ConversationFragment? = null
    private var mContactFrag: ContactFragment? = null
    private var mSettingsFrag: SettingsFragment? = null


    override fun initView() {
        mToolbar = mainToolbar
        mIvAdd = ivMainAdd
        mIvSearch = ivMainSearch
        mBottomNav = mainBottomNav

        setSupportActionBar(mToolbar)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDefaultFragment()
        connect()
        setClickListener()
    }

    private fun connect() {
        val user = BmobUser.getCurrentUser(User::class.java)
        if (user.objectId.isNotEmpty()) {
            BmobIM.connect(user.objectId, object: ConnectListener(){
                override fun done(p0: String?, p1: BmobException?) {
                    if (p1 == null) {
                        EventBus.getDefault().post(RefreshEvent())
                        BmobIM.getInstance().updateUserInfo(BmobIMUserInfo(user.objectId,
                                user.username, user.getAvatar()))
                        PLog.e("connect success")
                    } else {
                        ToastUtil.show(this@MainActivity, p1.message.toString())
                    }
                }
            })

            BmobIM.getInstance().setOnConnectStatusChangeListener(object: ConnectStatusChangeListener() {
                override fun onChange(p0: ConnectionStatus?) {
                    ToastUtil.show(this@MainActivity, p0!!.msg)
                }
            })
        }
    }

    private fun setClickListener() {
        mIvSearch.setOnClickListener { startActivity(this, SearchActivity::class.java) }

        mIvAdd.setOnClickListener {
            val popupMenu = PopupMenu(this, mIvAdd)
            popupMenu.inflate(R.menu.item_menu_add)
            popupMenu.setOnMenuItemClickListener {
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }

        mBottomNav.setOnNavigationItemSelectedListener {
            val transcation = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.mainConversation -> {
                    if (mConversationFrag == null) {
                        mConversationFrag = ConversationFragment()
                    }
                    transcation.replace(R.id.mainFrameLayout, mConversationFrag)
                }

                R.id.mainContact -> {
                    if (mContactFrag == null) {
                        mContactFrag = ContactFragment()
                    }
                    transcation.replace(R.id.mainFrameLayout, mContactFrag)
                }

                R.id.mainSettings -> {
                    if (mSettingsFrag == null) {
                        mSettingsFrag = SettingsFragment()
                    }
                    transcation.replace(R.id.mainFrameLayout, mSettingsFrag)
                }
            }
            transcation.commit()
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setDefaultFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        mConversationFrag = ConversationFragment()
        transaction.add(R.id.mainFrameLayout, mConversationFrag)
        transaction.commit()
    }

}
