package com.flowerasny.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast

const val TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION"
const val EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM"

class MyAppWidgetProvider : AppWidgetProvider() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("WidgetTag", "update")
        appWidgetIds.forEach { widgetId ->
            val intent = Intent(context, MyAppWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            val views = RemoteViews(context.packageName, R.layout.widget_content).apply {
                PendingIntent.getService(context, 0, Intent(context, MyAppWidgetService::class.java), 0)
                setRemoteAdapter(R.id.lvItems, intent)
                setEmptyView(R.id.lvItems, R.id.tvEmpty)
            }

            val toastPendingIntent: PendingIntent = Intent(
                context,
                MyAppWidgetProvider::class.java
            ).run {
                action = TOAST_ACTION
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))

                PendingIntent.getBroadcast(context, 0, this, FLAG_UPDATE_CURRENT)
            }
            views.setPendingIntentTemplate(R.id.lvItems, toastPendingIntent)

            appWidgetManager.updateAppWidget(widgetId, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        //val mgr: AppWidgetManager = AppWidgetManager.getInstance(context)
        if (intent.action == TOAST_ACTION) {
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
