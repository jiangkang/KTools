package com.jiangkang.storage.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by jiangkang on 2018/3/25.
 * description：
 */

class UserInfoDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION){

    companion object {
        val DB_NAME = "user-info"
        val DB_VERSION = 1
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UserInfoContract.SQL_CREATE_TABLE_USERINFO)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserInfoContract.SQL_DELETE_TABLE_USERINFO)
        onCreate(db)
    }


}