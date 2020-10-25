package com.jiangkang.ktools.receiver

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.jiangkang.ktools.MainActivity
import com.jiangkang.ktools.R

class KToolsAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds.forEach { appWidgetId ->
            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
                    .let { intent ->
                        PendingIntent.getActivity(context, 0, intent, 0)
                    }

            val views: RemoteViews = RemoteViews(
                    context.packageName,
                    R.layout.layout_ktools_widget
            ).apply {
                setOnClickPendingIntent(R.id.btn_action, pendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}