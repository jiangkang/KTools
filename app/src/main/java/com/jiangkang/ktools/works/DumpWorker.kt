package com.jiangkang.ktools.works

import android.content.Context
import android.os.Debug
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.File

class DumpWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    private val hprofFile = File(appContext.filesDir,"hprof_${System.currentTimeMillis()}.hprof")

    override suspend fun doWork(): Result {
        Debug.dumpHprofData(hprofFile.absolutePath)
        return Result.success()
    }
}