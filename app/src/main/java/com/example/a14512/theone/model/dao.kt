package com.example.a14512.theone.model

/**
 * @author 14512 on 2018/5/5
 */
//class DaoMaster(db: SQLiteDatabase): AbstractDaoMaster(db, SCHEMA_VERSION) {
//    init {
//        registerDaoClass(NewFriendDao::class.java)
//    }
//
//    companion object {
//        const val SCHEMA_VERSION = 1
//
//        fun createAllTables(db: SQLiteDatabase, ifNotExists: Boolean) {
//            NewFriendDao.createTable(db, ifNotExists)
//        }
//
//        fun dropAllTables(db: SQLiteDatabase, ifExists: Boolean) {
//            NewFriendDao.dropTable(db, ifExists)
//        }
//    }
//
//    override fun newSession(): DaoSession {
//        return DaoSession(db, IdentityScopeType.Session, daoConfigMap)
//    }
//
//    override fun newSession(p0: IdentityScopeType): DaoSession {
//        return DaoSession(db, p0, daoConfigMap)
//    }
//
//    abstract class OpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?):
//            SQLiteOpenHelper(context, name, factory, SCHEMA_VERSION) {
//
//        override fun onCreate(db: SQLiteDatabase) {
//            createAllTables(db, false)
//        }
//    }
//
//    class DevOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?):
//            OpenHelper(context, name, factory) {
//
//        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//            dropAllTables(db, true)
//            onCreate(db)
//        }
//    }
//
//}
//
//class NewFriendDao(config: DaoConfig, daoSession: DaoSession):
//        AbstractDao<NewFriend, Long>(config, daoSession) {
//
//    object Properties {
//        val Id = Property(0, Long::class.java, "id", true, "_id")
//        val Uid = Property(1, String::class.java, "uid", false, "UID")
//        val Msg = Property(2, String::class.java, "msg", false, "MSG")
//        val Name = Property(3, String::class.java, "name", false, "NAME")
//        val Avatar = Property(4, String::class.java, "avatar", false, "AVATAR")
//        val Status = Property(5, Integer::class.java, "status", false, "STATUS")
//        val Time = Property(6, Long::class.java, "time", false, "TIME")
//    }
//
//    constructor(config: DaoConfig): this(config, null as DaoSession)
//
//    companion object {
//        const val TABLENAME = "newfriend"
//
//        /** Creates the underlying database table. */
//        fun createTable(db: SQLiteDatabase, ifNotExists: Boolean) {
//            val constraint = if (ifNotExists) "IF NOT EXISTS " else ""
//            db.execSQL("CREATE TABLE " + constraint + "\"newfriend\" (" + //
//
//                    "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
//
//                    "\"UID\" TEXT," + // 1: uid
//
//                    "\"MSG\" TEXT," + // 2: msg
//
//                    "\"NAME\" TEXT," + // 3: name
//
//                    "\"AVATAR\" TEXT," + // 4: avatar
//
//                    "\"STATUS\" INTEGER," + // 5: status
//
//                    "\"TIME\" INTEGER);") // 6: time
//        }
//
//        /** Drops the underlying database table. */
//        fun dropTable(db: SQLiteDatabase, ifExists: Boolean) {
//            val sql = "DROP TABLE " + (if (ifExists) "IF EXISTS " else "") + "\"newfriend\""
//            db.execSQL(sql)
//        }
//
//    }
//    override fun readEntity(cursor: Cursor, offset: Int): NewFriend {
//        return NewFriend(
//                if (cursor.isNull(offset + 0)) null else cursor.getLong(offset + 0), // id
//                if (cursor.isNull(offset + 1)) null else cursor.getString(offset + 1), // uid
//                if (cursor.isNull(offset + 2)) null else cursor.getString(offset + 2), // msg
//                if (cursor.isNull(offset + 3)) null else cursor.getString(offset + 3), // name
//                if (cursor.isNull(offset + 4)) null else cursor.getString(offset + 4), // avatar
//                if (cursor.isNull(offset + 5)) null else cursor.getInt(offset + 5), // status
//                if (cursor.isNull(offset + 6)) null else cursor.getLong(offset + 6) // time
//        )
//    }
//
//    override fun readEntity(cursor: Cursor?, entity: NewFriend?, offset: Int) {
//        if (cursor != null && entity != null) {
//            entity.id = if (cursor.isNull(offset + 0)) null else cursor.getLong(offset + 0)
//            entity.uid = if (cursor.isNull(offset + 1)) null else cursor.getString(offset + 1)
//            entity.msg = if (cursor.isNull(offset + 2)) null else cursor.getString(offset + 2)
//            entity.name = if (cursor.isNull(offset + 3)) null else cursor.getString(offset + 3)
//            entity.avatar = if (cursor.isNull(offset + 4)) null else cursor.getString(offset + 4)
//            entity.status = if (cursor.isNull(offset + 5)) null else cursor.getInt(offset + 5)
//            entity.time = if (cursor.isNull(offset + 6)) null else cursor.getLong(offset + 6)
//        }
//    }
//
//    override fun updateKeyAfterInsert(p0: NewFriend?, p1: Long): Long {
//        p0?.id = p1
//        return p1
//    }
//
//    override fun bindValues(p0: SQLiteStatement?, p1: NewFriend?) {
//        if (p0 != null && p1 != null) {
//            p0.clearBindings()
//            val id = p1.id
//            if (id != null) {
//                p0.bindLong(1, id)
//            }
//
//            val uid = p1.uid
//            if (uid != null) {
//                p0.bindString(2, uid)
//            }
//
//            val msg = p1.msg
//            if (msg != null) {
//                p0.bindString(3, msg)
//            }
//
//            val name = p1.name
//            if (name != null) {
//                p0.bindString(4, name)
//            }
//
//            val avatar = p1.avatar
//            if (avatar != null) {
//                p0.bindString(5, avatar)
//            }
//
//            val status = p1.status
//            if (status != null) {
//                p0.bindLong(6, status.toLong())
//            }
//
//            val time = p1.time
//            if (time != null) {
//                p0.bindLong(7, time)
//            }
//        }
//    }
//
//    override fun isEntityUpdateable(): Boolean {
//        return true
//    }
//
//    override fun readKey(p0: Cursor?, p1: Int): Long? {
//        return if (p0?.isNull(p1 + 0) == null) null else p0.getLong(p1 + 0)
//    }
//
//    override fun getKey(p0: NewFriend?): Long? {
//        return p0?.id
//    }
//
//}
//
//class DaoSession(db: SQLiteDatabase, type: IdentityScopeType,
//                 daoConfigMap:  Map<Class<out AbstractDao<*, *>>, DaoConfig>): AbstractDaoSession(db) {
//
//    private var newFriendDaoConfig: DaoConfig = daoConfigMap[NewFriendDao::class.java]!!.clone()
//    private var newFriendDao: NewFriendDao
//
//
//    init {
//        newFriendDaoConfig.initIdentityScope(type)
//        newFriendDao = NewFriendDao(this.newFriendDaoConfig, this)
//        registerDao(NewFriend::class.java, newFriendDao)
//    }
//
//    fun clear() {
//        newFriendDaoConfig.identityScope?.clear()
//    }
//
//    fun getNewFriendDao(): NewFriendDao {
//        return newFriendDao
//    }
//
//}