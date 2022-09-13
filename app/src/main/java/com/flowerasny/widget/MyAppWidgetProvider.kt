package com.flowerasny.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast

const val REFRESH_ACTION = "com.flowerasny.widget.REFRESH_ACTION"
const val TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION"
const val EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM"

class MyAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("WidgetTag", "update")
        appWidgetIds.forEach { widgetId ->
            val intent = Intent(context, MyAppWidgetService::class.java).apply {
                putExtra(EXTRA_APPWIDGET_ID, widgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            val views = RemoteViews(context.packageName, R.layout.widget_content).apply {
                setRemoteAdapter(R.id.lvItems, intent)
                setEmptyView(R.id.lvItems, R.id.tvEmpty)
            }

            val refreshPendingIntent: PendingIntent = Intent(
                context,
                MyAppWidgetProvider::class.java
            ).run {
                action = REFRESH_ACTION
                putExtra(EXTRA_APPWIDGET_ID, widgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))



                PendingIntent.getBroadcast(context, 0, this, getMutableUpdateFlag())
            }
            views.setOnClickPendingIntent(R.id.btnRefresh, refreshPendingIntent)

            val toastPendingIntent: PendingIntent = Intent(
                context,
                MyAppWidgetProvider::class.java
            ).run {
                action = TOAST_ACTION
                putExtra(EXTRA_APPWIDGET_ID, widgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))

                PendingIntent.getBroadcast(context, 0, this, getMutableUpdateFlag())
            }
            views.setPendingIntentTemplate(R.id.lvItems, toastPendingIntent)

            appWidgetManager.updateAppWidget(widgetId, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun getMutableUpdateFlag() = when {
        SDK_INT >= S -> FLAG_UPDATE_CURRENT or FLAG_MUTABLE
        else -> FLAG_UPDATE_CURRENT
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == REFRESH_ACTION) {
            val widgetManager: AppWidgetManager = AppWidgetManager.getInstance(context)
            widgetManager.getAppWidgetIds(ComponentName(context, MyAppWidgetProvider::class.java))
                .forEach { widgetManager.notifyAppWidgetViewDataChanged(it, R.id.lvItems) }
        } else if (intent.action == TOAST_ACTION) {
            /*val widgetId: Int = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )*/

            val viewIndex: Int = intent.getIntExtra(EXTRA_ITEM, 0)
            Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
        }
        super.onReceive(context, intent)
    }
}
