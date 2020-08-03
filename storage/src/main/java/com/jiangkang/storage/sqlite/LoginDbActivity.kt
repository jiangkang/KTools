package com.jiangkang.storage.sqlite

import android.app.Activity
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.BaseColumns
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.jiangkang.storage.R
import com.jiangkang.tools.utils.ToastUtils
import kotlin.concurrent.thread

class LoginDbActivity : Activity() {

    private val etPassword:EditText by lazy { findViewById<EditText>(R.id.et_password) }
    private val etUsername: EditText by lazy { findViewById<EditText>(R.id.et_username) }
    
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
        if (TextUtils.isEmpty(etUsername.text) || TextUtils.isEmpty(etPassword.text)) {
            ToastUtils.showShortToast("用户名或密码不能为空")
            return
        }

        val db = dbHelper.writableDatabase


        val selection = "${UserInfoContract.UserInfoEntity.TABLE_NAME_USERNAME} = ?"

        val selectionArgs = arrayOf(etUsername.text.toString())


        thread {
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
        }.start()
    }


    private fun handleClick() {
        findViewById<Button>(R.id.btn_login_db).setOnClickListener {
            handleLogic(
                    present = {
                        with(it) {
                            while (moveToNext()) {
                                val pwd = getString(getColumnIndexOrThrow(UserInfoContract.UserInfoEntity.TABLE_NAME_PASSWORD))
                                runOnUiThread {
                                    if (etPassword.text.toString() == pwd) {
                                        ToastUtils.showShortToast("登录成功")
                                        finish()
                                    } else {
                                        ToastUtils.showShortToast("密码错误")
                                    }
                                }
                            }
                        }

                    },
                    absent = {
                        ToastUtils.showShortToast("您的账号还没有注册，请先进行注册")

                    }
            )
        }


        findViewById<Button>(R.id.btn_register_db).setOnClickListener {
            handleLogic(
                    present = {
                        ToastUtils.showShortToast("您的账户已经存在，请直接登录")
                    },
                    absent = {
                        insertDb(dbHelper.readableDatabase)
                    }
            )
        }

    }

    private fun insertDb(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(UserInfoContract.UserInfoEntity.TABLE_NAME_USERNAME, etUsername.text.toString())
            put(UserInfoContract.UserInfoEntity.TABLE_NAME_PASSWORD, etPassword.text.toString())
            put(UserInfoContract.UserInfoEntity.TABLE_NAME_LOGIN_TIME_LATEST, System.currentTimeMillis().toString())
        }

        db.insert(
                UserInfoContract.UserInfoEntity.TABLE_NAME,
                null,
                values
        )

        ToastUtils.showShortToast("注册成功")

    }
}
