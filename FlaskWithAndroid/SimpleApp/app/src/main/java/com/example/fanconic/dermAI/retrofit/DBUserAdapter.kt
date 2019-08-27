package com.example.fanconic.dermAI.retrofit

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.util.Log

class DBUserAdapter(ctx: Context) {

    private var context: Context? = null
    private val DBHelper: DatabaseHelper
    private var db: SQLiteDatabase? = null

    init {
        this.context = ctx
        DBHelper = DatabaseHelper(context!!)
    }

    private class DatabaseHelper internal constructor(context: Context) : SQLiteOpenHelper(context, Environment.getExternalStorageDirectory().path+ "/$DATABASE_NAME", null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(DATABASE_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data")
            db.execSQL("DROP TABLE IF EXISTS users")
            onCreate(db)
        }
    }


    @Throws(SQLException::class)
    fun open() {
        db = DBHelper.writableDatabase
    }


    fun close() {
        DBHelper.close()
    }

    fun AddUser(username: String, password: String): Long {
        val initialValues = ContentValues()
        initialValues.put(KEY_USERNAME, username)
        initialValues.put(KEY_PASSWORD, password)
        return db!!.insert(DATABASE_TABLE, null, initialValues)

    }

    @Throws(SQLException::class)
    fun Login(username: String, password: String): Boolean {
        val mCursor = db!!.rawQuery("SELECT * FROM $DATABASE_TABLE WHERE username=? AND password=?", arrayOf(username, password))
        if (mCursor != null) {
            if (mCursor.count > 0) {
                return true
            }
        }
        return false
    }

    companion object {
        val KEY_ROWID = "_id"
        val KEY_USERNAME = "username"
        val KEY_PASSWORD = "password"
        private val TAG = "DBAdapter"

        private val DATABASE_NAME = "usersdb"
        private val DATABASE_TABLE = "users"
        private val DATABASE_VERSION = 1

        private val DATABASE_CREATE = (
                "create table users (_id integer primary key autoincrement, "
                        + "username text not null, "
                        + "password text not null);")
    }

}