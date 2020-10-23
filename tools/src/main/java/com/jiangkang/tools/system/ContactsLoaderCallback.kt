package com.jiangkang.tools.system

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.jiangkang.tools.struct.JsonGenerator
import okhttp3.internal.closeQuietly
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

/**
 * Created by jiangkang on 2017/9/13.
 */
class ContactsLoaderCallback(private val context: Context) : LoaderManager.LoaderCallbacks<Cursor> {
    private var result: JSONObject? = null
    private var listener: QueryListener? = null
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        //指定获取_id和display_name两列数据，display_name即为姓名
        val projection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        )
        return CursorLoader(
                context,
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                null
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        if (data.isClosed) {
            return
        }
        thread {
            handleCursor(data)
        }
    }

    private fun handleCursor(data: Cursor?) {
        val jsonArray = JSONArray()
        if (data != null && data.moveToFirst()) {
            do {
                val name = data.getString(
                        data.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                )
                val id = data.getInt(
                        data.getColumnIndex(ContactsContract.Contacts._ID)
                )

                //指定获取NUMBER这一列数据
                val phoneProjection = arrayOf(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                )
                val cursor = context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        phoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null,
                        null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        val number = cursor.getString(
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )
                        jsonArray.put(
                                JsonGenerator()
                                        .put("name", name)
                                        .put("tel", number)
                                        .gen()
                        )
                    } while (cursor.moveToNext())
                }
                cursor?.closeQuietly()
            } while (data.moveToNext())
        }

        result = JsonGenerator()
                .put("list", jsonArray)
                .gen()
        listener?.success(result)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}
    fun setQueryListener(listener: QueryListener?) {
        this.listener = listener
    }

    interface QueryListener {
        fun success(json: JSONObject?)
    }
}