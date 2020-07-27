package com.jiangkang.tools.system

import android.app.Activity
import android.provider.ContactsContract
import android.util.Log
import org.json.JSONObject
import java.util.concurrent.CountDownLatch

/**
 * Created by jiangkang on 2017/9/8.
 */
class ContactHelper(private val context: Activity) {
    private var contacts: JSONObject? = null
    fun queryContactList(): JSONObject? {
        val latch = CountDownLatch(1)
        val callback = ContactsLoaderCallback(context)
        callback.setQueryListener(object : ContactsLoaderCallback.QueryListener{
            override fun success(json: JSONObject?) {
                contacts = json
                latch.countDown()
            }
        })
        context.loaderManager.restartLoader(0, null, callback)
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return contacts
    }

    fun pickContact(): JSONObject? {
        return null
    }

    fun queryContactNameList() {
        val cursor = context.contentResolver
                .query(
                        ContactsContract.Contacts.CONTENT_URI, arrayOf(
                        ContactsContract.Contacts.DISPLAY_NAME
                ),
                        null,
                        null,
                        null
                )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val name = cursor.getString(nameIndex)
                Log.d(TAG, "queryContactNameList: \n name = $name")
            } while (cursor.moveToNext())
        }
    }

    companion object {
        private const val TAG = "ContactHelper"
    }

}