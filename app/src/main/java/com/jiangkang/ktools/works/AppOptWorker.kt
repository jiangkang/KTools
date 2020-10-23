package com.jiangkang.ktools.works

import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.storage.StorageManager
import android.util.Log
import androidx.constraintlayout.motion.widget.Debug
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.work.*
import com.jiangkang.tools.utils.ShellUtils
import com.jiangkang.tools.utils.ToastUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.*

class AppOptWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val unitSizeM = 1024 * 1024
    private val unitSizeK = 1024

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "app_info")

    override fun doWork(): Result {
        val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        for (volume in storageManager.storageVolumes) {
            val uuid = if (volume.uuid == null) StorageManager.UUID_DEFAULT else UUID.fromString(volume.uuid)
            val uid = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).uid
            val stats = storageStatsManager.queryStatsForUid(uuid, uid)
            val appSize = stats.appBytes / unitSizeM
            val cacheSize = stats.cacheBytes / unitSizeK
            val dataSize = stats.dataBytes / unitSizeM
            val msg = "${context.packageName}:appSize = $appSize, cacheSize=$cacheSize, dataSize = $dataSize"
            GlobalScope.launch {
                dataStore.edit {
                   it[preferencesKey<String>("app_size")] = "${appSize}M"
                   it[preferencesKey<String>("data_size")] = "${dataSize}M"
                   it[preferencesKey<String>("cache_size")] = "${cacheSize}K"
                }
            }
            Log.d("Worker", msg)
        }
        ShellUtils.execCmd("adb shell cmd package compile -m speed ${context.packageName}", false)
        return Result.success()
    }
    
    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val constraints = Constraints.Builder()
                    .setRequiresDeviceIdle(true)
                    .build()

            val request = PeriodicWorkRequestBuilder<AppOptWorker>(Duration.ofSeconds(5))
                    .setConstraints(constraints)
                    .setInitialDelay(Duration.ofSeconds(10))
                    .build()
            WorkManager.getInstance(context).enqueue(request)
        }

    }
}