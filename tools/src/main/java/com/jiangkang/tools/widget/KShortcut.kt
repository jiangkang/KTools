package com.jiangkang.tools.widget

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

object KShortcut {

    fun createShortcutInfo(context: Context,
                           id:String,
                           shortName: String, longName: String = shortName,
                           @DrawableRes iconRes: Int, intent: Intent): ShortcutInfoCompat {
        return ShortcutInfoCompat.Builder(context, id)
                .setShortLabel(shortName)
                .setLongLabel(longName)
                .setIcon(IconCompat.createWithResource(context, iconRes))
                .setIntent(intent.apply { action = Intent.ACTION_VIEW })
                .build()
    }

    fun addShortcut(context: Context,
                    id:String,
                    shortName: String, longName: String = shortName,
                    @DrawableRes iconRes: Int, intent: Intent){
        val shortcut = createShortcutInfo(context, id, shortName, longName, iconRes, intent)
        ShortcutManagerCompat.addDynamicShortcuts(context, listOf(shortcut))
    }

}