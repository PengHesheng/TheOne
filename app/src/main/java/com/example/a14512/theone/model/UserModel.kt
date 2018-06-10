package com.example.a14512.theone.model

import cn.bmob.newim.BmobIM
import cn.bmob.newim.bean.BmobIMConversation
import cn.bmob.newim.event.MessageEvent
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.example.a14512.theone.model.i.QueryUserListener
import com.example.a14512.theone.model.i.UpdateCacheListener
import com.example.a14512.theone.utils.PLog

/**
 * @author 14512 on 2018/5/2
 */
class UserModel : BaseModel() {

    companion object {
        private val OUR_INSTANCE: UserModel = UserModel()

        fun getInstance(): UserModel {
            return OUR_INSTANCE
        }
    }


    /**
     * register
     *
     * @param userName
     * @param pwd
     * @param pwdAgain
     * @param listener
     */
    fun register(userName: String, pwd: String, pwdAgain: String, listener: LogInListener<User>) {
        val user = User()
        user.username = userName
        user.setPassword(pwd)
        user.signUp(object : SaveListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                if (e == null) {
                    listener.done(null, null)
                } else {
                    listener.done(null, e)
                }
            }
        })
    }

    /**
     * login
     *
     * @param userName
     * @param pwd
     * @param listener
     */
    fun login(userName: String, pwd: String, listener: LogInListener<User>) {
        val user = User()
        user.username = userName
        user.setPassword(pwd)
        user.login(object : SaveListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                if (e == null) {
                    listener.done(getCurrentUser(), null)
                } else {
                    listener.done(user, e)
                }
            }
        })
    }

    /**
     * log out
     */
    fun logOut() {
        BmobUser.logOut()
    }

    /**
     * 获取当前用户
     */
    fun getCurrentUser(): User? {
        return BmobUser.getCurrentUser(User::class.java)
    }

    /**
     * 查询用户
     *
     * @param userName
     * @param limit
     * @param listener
     */
    fun queryUsers(userName: String, limit: Int, listener: FindListener<User>) {
        val query = BmobQuery<User>()
        //去掉当前用户
        try {
            val user = BmobUser.getCurrentUser()
            query.addWhereEqualTo("username", user.username)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        query.addWhereEqualTo("username", userName)
        query.setLimit(limit)
        query.order("-createdAt")
        query.findObjects(object : FindListener<User>() {
            override fun done(list: List<User>?, e: BmobException?) {
                PLog.e(list!!.size.toString()+"\t"+e.toString())
                if (e == null) {
                    if (list != null && list.isNotEmpty()) {
                        listener.done(list, e)
                    } else {
                        listener.done(list, BmobException(CODE_NULL, "查无此人"))
                    }
                } else {
                    listener.done(list, e)
                }
            }
        })
    }

    /**
     * 查询指定用户信息
     *
     * @param objectId
     * @param listener
     */
    fun queryUserInfo(objectId: String, listener: QueryUserListener) {
        val query = BmobQuery<User>()
        query.addWhereEqualTo("objectId", objectId)
        query.findObjects(object : FindListener<User>() {
            override fun done(list: List<User>?, e: BmobException?) {
                if (e == null) {
                    if (list != null && list.isNotEmpty()) {
                        listener.done(list[0], null)
                    } else {
                        listener.done(null, BmobException(CODE_NULL, "查无此人"))
                    }
                } else {
                    listener.done(null, e)
                }
            }
        })
    }

    /**
     * 更新用户资料和会话资料
     *
     * @param event
     * @param listener
     */
    fun updateUserInfo(event: MessageEvent, listener: UpdateCacheListener) {
        val conversation: BmobIMConversation = event.conversation
        val info = event.fromUserInfo
        val msg = event.message
        var userName = info.name
        var avatar = info.avatar
        var title = conversation.conversationTitle
        var icon = conversation.conversationIcon
        //SDK内部将新会话的会话标题用objectId表示，因此需要比对用户名和私聊会话标题，后续会根据会话类型进行判断
        if (userName != title || avatar != icon) {
            UserModel.getInstance().queryUserInfo(info.userId, object : QueryUserListener() {
                override fun done(s: User?, e: BmobException?) {
                    if (e == null && s != null) {
                        var name = s.username
                        var avatar = s.getAvatar()
                        conversation.conversationIcon = avatar
                        conversation.conversationTitle = name
                        info.name = name
                        info.avatar = avatar
                        // 用户管理：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().updateUserInfo(info)
                        // 会话：4.7、更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if (!msg.isTransient) {
                            BmobIM.getInstance().updateConversation(conversation)
                        }
                    } else {
                        PLog.e(e.toString())
                    }
                    listener.done(null)
                }
            })
        } else {
            listener.done(null)
        }
    }

    /**
     * 好友管理，添加好友
     *
     * @param friend
     * @param listener
     */
    fun agreeAddFriend(friend: User, listener: SaveListener<String>) {
        var f = Friend()
        var user = getCurrentUser()
        f.setUser(user!!)
        f.setFriendUser(friend)
        f.save(listener)
    }

    /**
     * 查询好友
     *
     * @param listener
     */
    fun queryFriends(listener: FindListener<Friend>) {
        var query: BmobQuery<Friend> = BmobQuery()
        var user = getCurrentUser()
        query.addWhereEqualTo("user", user)
        query.include("friendUser")
        query.order("-updateAt")
        query.findObjects(object : FindListener<Friend>() {
            override fun done(list: List<Friend>?, e: BmobException?) {
                if (e == null) {
                    if (list != null && list.isNotEmpty()) {
                        listener.done(list, e)
                    } else {
                        listener.done(list, BmobException(CODE_NULL, "暂无联系人"))
                    }
                } else {
                    listener.done(list, e)
                }
            }
        })
    }

    /**
     * 删除好友
     *
     * @param friend
     * @param listener
     */
    fun delFriend(friend: Friend, listener: UpdateListener) {
        var f = Friend()
        f.delete(friend.objectId, listener)
    }
}