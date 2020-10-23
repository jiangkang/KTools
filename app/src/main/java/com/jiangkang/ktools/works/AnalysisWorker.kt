package com.jiangkang.ktools.works

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AnalysisWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        
        return Result.success()
    }
    
}