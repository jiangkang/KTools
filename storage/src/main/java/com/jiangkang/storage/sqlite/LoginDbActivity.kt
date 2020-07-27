package com.jiangkang.storage.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.text.TextUtils
import com.jiangkang.storage.R
import kotlinx.android.synthetic.main.activity_login_db.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class LoginDbActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserInfoDbHelper

    private val projection = arrayOf(
            BaseColumns._ID,
            UserInfoContract.UserInfoEntity.TABLE_NAME_USERNAME,
            UserInfoContract.UserInfoEntity.TABLE_NAME_PASSWORD,
            UserInfoContract.UserInfoEntity.TABLE_NAME_LOGIN_TIME_LATEST
    )


    private val sortOder = "${UserInfoContract.UserInfoEntity.TABLE_NAME_USERNAME} DESC"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_db)
        title = "SQLite Demo"
        dbHelper = UserInfoDbHelper(this)
        handleClick()
    }

    private fun handleLogic(present: ((cursor: Cursor) -> Unit), absent: (() -> Unit)) {
        if (TextUtils.isEmpty(et_username.text) || TextUtils.isEmpty(et_password.text)) {
            toast("用户名或密码不能为空").show()
            return
        }

        val db = dbHelper.writableDatabase


        val selection = "${UserInfoContract.UserInfoEntity.TABLE_NAME_USERNAME} = ?"

        val selectionArgs = arrayOf(et_username.text.toString())


        doAsync {
            val cursor = db.query(
                    UserInfoContract.UserInfoEntity.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOder
            )

            if (cursor == null || cursor.count == 0) {
                absent()

            } else {
                present(cursor)
            }

        }

    }


    private fun handleClick() {
        btn_login_db.setOnClickListener {
            handleLogic(
                    present = {
                        with(it) {
                            while (moveToNext()) {
                                val pwd = getString(getColumnIndexOrThrow(UserInfoContract.UserInfoEntity.TABLE_NAME_PASSWORD))
                                runOnUiThread {
                                    if (et_password.text.toString() == pwd) {
                                        toast("登录成功").show()
                                        finish()
                                    } else {
                                        toast("密码错误").show()
                                    }
                                }
                            }
                        }

                    },
                    absent = {
                        runOnUiThread {
                            toast("您的账号还没有注册，请先进行注册").show()
                        }

                    }
            )
        }


        btn_register_db.setOnClickListener {
            handleLogic(
                    present = {
                        runOnUiThread {
                            toast("您的账户已经存在，请直接登录").show()
                        }

                    },
                    absent = {
                        insertDb(dbHelper.readableDatabase)
                    }
            )
        }

    }

    private fun insertDb(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(UserInfoContract.UserInfoEntity.TABLE_NAME_USERNAME, et_username.text.toString())
            put(UserInfoContract.UserInfoEntity.TABLE_NAME_PASSWORD, et_password.text.toString())
            put(UserInfoContract.UserInfoEntity.TABLE_NAME_LOGIN_TIME_LATEST, System.currentTimeMillis().toString())
        }

        db.insert(
                UserInfoContract.UserInfoEntity.TABLE_NAME,
                null,
                values
        )

        runOnUiThread {
            toast("注册成功").show()
        }

    }
}
