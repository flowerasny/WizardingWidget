package com.flowerasny.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews

class MyAppWidgetProvider : AppWidgetProvider() {

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

            appWidgetManager.updateAppWidget(widgetId, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}
