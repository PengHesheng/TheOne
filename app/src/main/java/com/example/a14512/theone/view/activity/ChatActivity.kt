package com.example.a14512.theone.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.*
import cn.bmob.newim.core.BmobIMClient
import cn.bmob.newim.core.BmobRecordManager
import cn.bmob.newim.event.MessageEvent
import cn.bmob.newim.listener.MessageListHandler
import cn.bmob.newim.listener.MessageSendListener
import cn.bmob.newim.listener.MessagesQueryListener
import cn.bmob.newim.listener.OnRecordChangeListener
import cn.bmob.newim.notification.BmobNotificationManager
import cn.bmob.v3.exception.BmobException
import com.example.a14512.theone.R
import com.example.a14512.theone.adapter.ChatAdapter
import com.example.a14512.theone.adapter.OnRecyclerChatItemClickListener
import com.example.a14512.theone.base.BaseActivity
import com.example.a14512.theone.utils.KeyBoradUtil
import com.example.a14512.theone.utils.PLog
import com.example.a14512.theone.utils.SDCardUtil
import com.example.a14512.theone.utils.ToastUtil
import com.example.a14512.theone.view.IChatView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.include_chat_bottom_key_broad.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @author 14512 on 2018/5/21
 */
class ChatActivity : BaseActivity(), IChatView, MessageListHandler {

    private lateinit var mLayoutChat: LinearLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSwipe: SwipeRefreshLayout
    private lateinit var mLayoutRecord: RelativeLayout
    private lateinit var mIvRecord: ImageView
    private lateinit var mTvHint: TextView
    private lateinit var mIvBack: ImageView
    private lateinit var mIBtnVoice: ImageButton
    private lateinit var mBtnSpeak: Button
    private lateinit var mEtContent: EditText
    private lateinit var mBtnFace: Button
    private lateinit var mBtnAdd: Button
    private lateinit var mBtnSend: Button
    private lateinit var mLayoutBottom: LinearLayout
    private lateinit var mViewPager: ViewPager
    private var isVoiceClicked: Boolean = true
    private var isFaceClicked: Boolean = true
    private var isAddClicked: Boolean = true
    private lateinit var mConversationManager: BmobIMConversation
    private lateinit var mRecordManager: BmobRecordManager
    private lateinit var mAdapter: ChatAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAnims: ArrayList<Drawable>
    private var mSendListener = object : MessageSendListener() {

        override fun onProgress(p0: Int) {
            super.onProgress(p0)
            PLog.i(p0.toString())
        }

        override fun onStart(p0: BmobIMMessage?) {
            super.onStart(p0)
            if (p0 != null) {
                mAdapter.addMsg(p0)
                mEtContent.setText("")
                scrollToBottom()
            }
        }

        override fun done(p0: BmobIMMessage?, p1: BmobException?) {
            mAdapter.notifyDataSetChanged()
            mEtContent.setText("")
            scrollToBottom()
            if (p1 != null) {
                toastMsg(p1.message.toString())
            }
        }

    }

    fun actionStart(context: Context, conversation: BmobIMConversation) {
        context.startActivity(Intent(context, ChatActivity::class.java)
                .putExtra("conversation", conversation))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView() {
        mLayoutChat = layoutChat
        mRecyclerView = chatRecycler
        mSwipe = chatSwipe
        mLayoutRecord = layoutRecordChat
        mIvRecord = ivSpeakRecordChat
        mTvHint = tvHintRecordChat
        val toolbar = toolbar
        val title = tvToolbarTitle
        mIvBack = ivToolbarLeft
        mIBtnVoice = btnChatVoice
        mBtnSpeak = btnChatSpeak
        mEtContent = etChat
        mBtnFace = btnChatLaughFace
        mBtnAdd = btnChatAdd
        mBtnSend = btnChatSend
        mLayoutBottom = layoutChatBottom
        mViewPager = viewPagerChat

        setSupportActionBar(toolbar)
        initRecycler()
        initVoice()
        val conversation = intent.getSerializableExtra("conversation") as BmobIMConversation
        if (conversation != null) {
            mConversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversation)
            title.text = mConversationManager.conversationTitle
            mAdapter.setConversation(mConversationManager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
    private fun initVoice() {
        mBtnSpeak.setOnTouchListener { v, event ->
            if (event != null && v != null) {
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (SDCardUtil.checkSdCard()) {
                            ToastUtil.show("发送语音需要sdcard支持！")
                            false
                        }
                        try {
                            v.isPressed = true
                            mLayoutRecord.visibility = View.VISIBLE
                            //开始录音
                            mRecordManager.startRecording(mConversationManager.conversationId)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (event.y < 0) {
                            mTvHint.setTextColor(Color.RED)
                        } else {
                            mTvHint.setTextColor(Color.WHITE)
                        }
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        v.isPressed = false
                        mLayoutRecord.visibility = View.GONE
                        try {
                            if (event.y < 0) {
                                mRecordManager.cancelRecording()
                                PLog.i("放弃发送")
                            } else {
                                val recordTime = mRecordManager.stopRecording()
                                if (recordTime > 1) {
                                    //发送语音文件
                                    sendVoiceMsg(mRecordManager
                                            .getRecordFilePath(mConversationManager.conversationId)
                                            , recordTime)
                                } else {
                                    //录音时间过短，提示
                                    mLayoutRecord.visibility = View.GONE
                                    toastMsg("录音时间太短，请重试！")
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        true
                    }
                    else -> { false }
                }
            } else {
                false
            }
        }
        mAnims = ArrayList()
        mAnims.add(resources.getDrawable(R.mipmap.chat_icon_voice1, null))
        mAnims.add(resources.getDrawable(R.mipmap.chat_icon_voice2, null))
        mAnims.add(resources.getDrawable(R.mipmap.chat_icon_voice3, null))
        mAnims.add(resources.getDrawable(R.mipmap.chat_icon_voice4, null))
        mAnims.add(resources.getDrawable(R.mipmap.chat_icon_voice5, null))
        mAnims.add(resources.getDrawable(R.mipmap.chat_icon_voice6, null))
        // 语音相关管理器
        mRecordManager = BmobRecordManager.getInstance(this)
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        mRecordManager.setOnRecordChangeListener(object : OnRecordChangeListener {
            override fun onVolumeChanged(p0: Int) {
                mIvRecord.setImageDrawable(mAnims[p0])
            }

            override fun onTimeChanged(p0: Int, p1: String?) {
                if (p0 >= BmobRecordManager.MAX_RECORD_TIME) {
                    // 1分钟结束，发送消息
                    // 需要重置按钮
                    mBtnSpeak.isPressed = false
                    mBtnSpeak.isClickable = false
                    // 取消录音框
                    mLayoutRecord.visibility = View.GONE
                    // 发送语音消息
                    sendVoiceMsg(p1!!, p0)
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    Handler().postDelayed({ mBtnSpeak.isClickable = true }, 1000)
                }
            }
        })
    }

    private fun initRecycler() {
        mAdapter = ChatAdapter()
        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter
    }

    override fun startActivity() {

    }

    override fun toastMsg(msg: String) {
        ToastUtil.show(this, msg)
    }

    //消息接收：8.2、单个页面的自定义接收器
    override fun onMessageReceive(p0: MutableList<MessageEvent>?) {
        if (p0 != null) {
            PLog.i("聊天页面接收到消息：" + p0.size)
            //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
            for (i in p0.indices) {
                addMessage2Chat(p0[i])
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setOnClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListener() {
        mLayoutChat.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                mLayoutChat.viewTreeObserver.removeOnGlobalLayoutListener(this)
                mSwipe.isRefreshing = true
                //自动刷新
                queryMsg(null)
            }
        })
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

        mBtnSend.setOnClickListener {
            sendTextMsg()
        }

        mEtContent.setOnClickListener {
            KeyBoradUtil.showSoftInputFromWindow(this, mEtContent)
            if (mLayoutBottom.visibility == View.VISIBLE) {
                mLayoutBottom.visibility = View.GONE
                mBtnSpeak.visibility = View.GONE
            }
        }
        mEtContent.setOnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_DOWN || event?.action == MotionEvent.ACTION_UP) {
                scrollToBottom()
            }
            false
        }
        mEtContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                scrollToBottom()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && !TextUtils.isEmpty(s)) {
                    mBtnAdd.visibility = View.GONE
                    mBtnSend.visibility = View.VISIBLE
                } else {
                    mBtnAdd.visibility = View.VISIBLE
                    mBtnSend.visibility = View.GONE
                }
            }

        })

        mSwipe.setOnRefreshListener {
            val msg = mAdapter.getFirstMsg()
            queryMsg(msg)
        }

        mAdapter.setOnItemClickListener(object : OnRecyclerChatItemClickListener {
            override fun onItemClick(view: View, position: Int) {

            }

            override fun onItemLongClick(view: View, position: Int): Boolean {
                return true
            }

            override fun onPortraitClick(portrait: View, position: Int) {
                val userInfo = mAdapter.getMsgs()[position].bmobIMUserInfo
                UserInfoActivity().actionStart(this@ChatActivity, userInfo)
            }

            override fun onPortraitLongClick(view: View, position: Int): Boolean {
                return true
            }

            override fun onResendClick(ivFailed: View, tvStatus: View, progress: View, position: Int) {
                var msg = mAdapter.getMsgs()[position]
                when(mAdapter.getItemViewType(position)) {
                    mAdapter.TYPE_SEND_IMAGE -> {
                        msg = BmobIMImageMessage.buildFromDB(true, msg)
                    }
                    mAdapter.TYPE_SEND_VOICE -> {
                        msg = BmobIMAudioMessage.buildFromDB(true, msg)
                    }
                    mAdapter.TYPE_SEND_LOCATION -> {
                        msg = BmobIMLocationMessage.buildFromDB(msg)
                    }
                }
                mConversationManager.sendMessage(msg, object : MessageSendListener() {
                    override fun onStart(p0: BmobIMMessage?) {
                        progress.visibility = View.VISIBLE
                        ivFailed.visibility = View.GONE
                        tvStatus.visibility = View.GONE
                        super.onStart(p0)
                    }

                    override fun done(p0: BmobIMMessage?, p1: BmobException?) {
                        if (p1 == null) {
                            (tvStatus as TextView).text = "已发送"
                            tvStatus.visibility = View.VISIBLE
                            ivFailed.visibility = View.GONE
                            progress.visibility = View.GONE
                        } else {
                            ivFailed.visibility = View.VISIBLE
                            progress.visibility = View.GONE
                            tvStatus.visibility = View.GONE
                        }
                    }

                })
            }

            override fun onContentClick(view: View, position: Int) {
                //TODO 消息点击事件
            }

            override fun onContentLongClick(view: View, position: Int): Boolean {
                return true
            }
        })
    }

    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     *
     * @param msg
     */
    private fun queryMsg(msg: BmobIMMessage?) {
        mConversationManager.queryMessages(msg, 10, object : MessagesQueryListener() {
            override fun done(p0: MutableList<BmobIMMessage>?, p1: BmobException?) {
                mSwipe.isRefreshing = false
                if (p1 == null) {
                    if (p0 != null && p0.isNotEmpty()) {
                        mAdapter.addMsg(p0)
                        mLayoutManager.scrollToPositionWithOffset(p0.size - 1, 0)
                    }
                } else {
                    toastMsg("${p1.message}(${p1.errorCode})")
                }
            }

        })
    }

    private fun scrollToBottom() {
        mLayoutManager.scrollToPositionWithOffset(mAdapter.itemCount - 1, 0)
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private fun addUnReadMessage() {
        val cache = BmobNotificationManager.getInstance(this).notificationCacheList
        if (cache.size > 0) {
            val size = cache.size
            for (i in 0 until size) {
                val event = cache[i]
                addMessage2Chat(event)
            }
        }
        scrollToBottom()
    }

    /**
     * 添加消息到聊天界面中
     *
     * @param event
     */
    private fun addMessage2Chat(event: MessageEvent) {
        val msg = event.message
        if (mConversationManager != null && event != null
                && mConversationManager.conversationId == event.conversation.conversationId //如果是当前会话的消息

                && !msg.isTransient) {//并且不为暂态消息
            if (mAdapter.findPosition(msg) < 0) {//如果未添加到界面中
                mAdapter.addMsg(msg)
                //更新该会话下面的已读状态
                mConversationManager.updateReceiveStatus(msg)
            }
            scrollToBottom()
        } else {
            PLog.e("不是与当前聊天对象的消息")
        }
    }

    private fun sendVoiceMsg(local: String?, length: Int) {
        if (local != null) {
            val audio = BmobIMAudioMessage(local)
            //可设置额外信息-开发者设置的额外信息，需要开发者自己从extra中取出来
            val map = HashMap<String, Any>()
            map["from"] = "优酷"
            audio.setExtraMap(map)
            //设置语音文件时长：可选
//        audio.setDuration(length);
            mConversationManager.sendMessage(audio, mSendListener)
        }
    }

    private fun sendTextMsg() {
        val content = mEtContent.text.toString()
        if (content.isEmpty()) {
            toastMsg("请输入内容！")
            return
        }
        //发送文本消息
        val msg = BmobIMTextMessage()
        msg.content = content
        //可随意设置额外信息
//        val map = java.util.HashMap<String, Any>()
//        map["level"] = "1"
//        msg.setExtraMap(map)
        mConversationManager.sendMessage(msg, mSendListener)
    }

    fun sendLocalImageMsg() {
        // 发送消息：6.2、发送本地图片消息 TODO 获取本地图片
        //正常情况下，需要调用系统的图库或拍照功能获取到图片的本地地址，开发者只需要将本地的文件地址传过去就可以发送文件类型的消息
        val image = BmobIMImageMessage("/storage/emulated/0/netease/cloudmusic/网易云音乐相册/小梦大半_1371091013186741.jpg")
        mConversationManager.sendMessage(image, mSendListener)
    }

    fun sendRemoteImageMsg() {
        // 发送消息：6.3、发送远程图片消息
        val image = BmobIMImageMessage()
        image.remoteUrl = "https://avatars3.githubusercontent.com/u/11643472?v=4&u=df609c8370b3ef7a567457eafd113b3ba6ba3bb6&s=400"
        mConversationManager.sendMessage(image, mSendListener)
    }

    private fun sendLocalAudioMsg() {
        // 发送消息：6.4、发送本地音频文件消息 TODO 本地音频
        val audio = BmobIMAudioMessage("此处替换为你本地的音频文件地址")
        mConversationManager.sendMessage(audio, mSendListener)
    }

    private fun sendRemoteAudioMsg() {
        // 发送消息：6.5、发送本地音频文件消息
        val audio = BmobIMAudioMessage()
        audio.remoteUrl = "此处替换为你远程的音频文件地址"
        mConversationManager.sendMessage(audio, mSendListener)
    }

    private fun sendLocalVideoMsg() {
        // 发送消息：6.6、发送本地视频文件消息 TODO 本地视频
        val video = BmobIMVideoMessage("此处替换为你本地的视频文件地址")
        mConversationManager.sendMessage(video, mSendListener)
    }

    private fun sendRemoteVideoMsg() {
        // 发送消息：6.7、发送本地音频文件消息
        val audio = BmobIMAudioMessage()
        audio.remoteUrl = "此处替换为你远程的音频文件地址"
        mConversationManager.sendMessage(audio, mSendListener)
    }

    fun sendLocalFileMsg() {
        // 发送消息：6.8、发送本地文件消息 TODO 本地文件
        val file = BmobIMFileMessage("此处替换为你本地的文件地址")
        mConversationManager.sendMessage(file, mSendListener)
    }

    fun sendRemoteFileMsg() {
        // 发送消息：6.9、发送远程文件消息
        val file = BmobIMFileMessage()
        file.remoteUrl = "此处替换为你远程的文件地址"
        mConversationManager.sendMessage(file, mSendListener)
    }

    fun sendLocationMsg() {
        //TODO 发送消息：6.10、发送位置消息
        //测试数据，真实数据需要从地图SDK中获取
        val location = BmobIMLocationMessage("广州番禺区", 23.5, 112.0)
        val map = java.util.HashMap<String, Any>()
        map["from"] = "百度地图"
        location.setExtraMap(map)
        mConversationManager.sendMessage(location, mSendListener)
    }

    override fun onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage()
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this)
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification()
        super.onResume()
    }

    override fun onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this)
        super.onPause()
    }

    override fun onDestroy() {
        //清理资源
        if (mRecordManager != null) {
            mRecordManager.clear()
        }
        // 消息：5.4、更新此会话的所有消息为已读状态
        if (mConversationManager != null) {
            mConversationManager.updateLocalCache()
        }
        KeyBoradUtil.hideInputFromWindow(this, mLayoutChat)
        super.onDestroy()
    }
}
