package com.jiangkang.storage.sqlite

import android.provider.BaseColumns

/**
 * Created by jiangkang on 2018/3/25.
 * description：
 */

object UserInfoContract {

    const val SQL_CREATE_TABLE_USERINFO =
            """
            CREATE TABLE ${UserInfoEntity.TABLE_NAME} (
              ${BaseColumns._ID} INTEGER PRIMARY KEY,
              ${UserInfoEntity.TABLE_NAME_USERNAME} TEXT,
              ${UserInfoEntity.TABLE_NAME_PASSWORD} TEXT,
              ${UserInfoEntity.TABLE_NAME_LOGIN_TIME_LATEST} TEXT
            )
            """

    const val SQL_DELETE_TABLE_USERINFO =
            """
            DROP TABLE IF EXISTS ${UserInfoEntity.TABLE_NAME}
            """

    object UserInfoEntity : BaseColumns {

        const val TABLE_NAME = "user_info"

        const val TABLE_NAME_USERNAME = "username"

        const val TABLE_NAME_PASSWORD = "password"

        const val TABLE_NAME_LOGIN_TIME_LATEST = "login_time_latest"
    }

}