package com.example.a14512.theone.model

/**
 * @author 14512 on 2018/5/5
 */
//class NewFriendManager private constructor(context: Context, uId: String) {
//    private var openHelper: DaoMaster.DevOpenHelper
//    private var mContext: Context
//    private var mUId: String = uId
//
//    private fun clear() {
//        if (openHelper != null) {
//            openHelper.close()
////            openHelper = null
////            mContext = null
////            mUId = null
//        }
//    }
//
//    companion object {
//        private val daoMap = HashMap<String, NewFriendManager>()
//
//        fun getInstance(context: Context): NewFriendManager{
//            val user = BmobUser.getCurrentUser()
//            val longId = user.objectId
//            if (longId.isEmpty()) {
//                throw RuntimeException("you must login")
//            }
//            var dao = daoMap[longId]
//            if (dao == null) {
//                dao = NewFriendManager(context, longId)
//                daoMap[longId] = dao
//            }
//            return dao
//        }
//    }
//
//    init {
//        clear()
//        mContext = context.applicationContext
//        var DBName = "$mUId.theonedb"
//        this.openHelper = DaoMaster.DevOpenHelper(mContext, DBName, null)
//    }
//
//    private fun openReadableDb(): DaoSession {
//        checkInit()
//        val db = openHelper.readableDatabase
//        val daoMaster = DaoMaster(db)
//        return daoMaster.newSession()
//    }
//
//    private fun openWriteableDb(): DaoSession {
//        checkInit()
//        val db = openHelper.writableDatabase
//        val daoMaster = DaoMaster(db)
//        return daoMaster.newSession()
//    }
//
//    private fun checkInit() {
//
//    }
//
//    //-------------------------------------------------------------
//
//    /**获取本地所有的邀请信息
//     * @return
//     */
//    fun getAllNewFriend(): List<NewFriend> {
//        val dao = openReadableDb().getNewFriendDao()
//        return dao.queryBuilder().orderDesc(NewFriendDao.Properties.Time).list()
//    }
//
//    /**创建或更新新朋友信息
//     * @param info
//     * @return long:返回插入或修改的id
//     */
//    fun insertOrUpdateNewFriend(info: NewFriend): Long {
//        val dao = openWriteableDb().getNewFriendDao()
//        val local = getNewFriend(info.uid!!, info.time)
//        return if (local == null) {
//            dao.insertOrReplace(info)
//        } else {
//            -1
//        }
//    }
//
//    /**
//     * 获取本地的好友请求
//     * @param uid
//     * @param time
//     * @return
//     */
//    private fun getNewFriend(uid: String, time: Long?): NewFriend? {
//        val dao = openReadableDb().getNewFriendDao()
//        return dao.queryBuilder().where(NewFriendDao.Properties.Uid.eq(uid))
//                .where(NewFriendDao.Properties.Time.eq(time)).build().unique()
//    }
//
//    /**
//     * 是否有新的好友邀请
//     * @return
//     */
//    fun hasNewFriendInvitation(): Boolean {
//        val infos = getNoVerifyNewFriend()
//        return infos != null && infos.isNotEmpty()
//    }
//
//    /**
//     * 获取未读的好友邀请
//     * @return
//     */
//    fun getNewInvitationCount(): Int {
//        val infos = getNoVerifyNewFriend()
//        return if (infos != null && infos.isNotEmpty()) {
//            infos.size
//        } else {
//            0
//        }
//    }
//
//    /**
//     * 获取所有未读未验证的好友请求
//     * @return
//     */
//    private fun getNoVerifyNewFriend(): List<NewFriend>? {
//        val dao = openReadableDb().getNewFriendDao()
//        return dao.queryBuilder().where(NewFriendDao.Properties.Status.eq(STATUS_VERIFY_NONE))
//                .build().list()
//    }
//
//    /**
//     * 批量更新未读未验证的状态为已读
//     */
//    fun updateBatchStatus() {
//        val infos = getNoVerifyNewFriend()
//        if (infos != null && infos.isNotEmpty()) {
//            val size = infos.size
//            val all = ArrayList<NewFriend>()
//            for (i in 0 until size) {
//                val msg = infos[i]
//                msg.status = STATUS_VERIFY_READED
//                all.add(msg)
//            }
//            insertBatchMessages(infos)
//        }
//    }
//
//    /**批量插入消息
//     * @param msgs
//     */
//    fun insertBatchMessages(msgs: List<NewFriend>) {
//        val dao = openWriteableDb().getNewFriendDao()
//        dao.insertOrReplaceInTx(msgs)
//    }
//
//    /**
//     * 修改指定好友请求的状态
//     * @param friend
//     * @param status
//     * @return
//     */
//    fun updateNewFriend(friend: NewFriend, status: Int): Long {
//        val dao = openWriteableDb().getNewFriendDao()
//        friend.status = status
//        return dao.insertOrReplace(friend)
//    }
//
//    /**
//     * 删除指定的添加请求
//     * @param friend
//     */
//    fun deleteNewFriend(friend: NewFriend) {
//        val dao = openWriteableDb().getNewFriendDao()
//        dao.delete(friend)
//    }
//
//}