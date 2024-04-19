package com.kisahcode.androidintermediate

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 *
 * This class serves as the App Widget provider for the Random Number Widget.
 * It handles the creation, update, and deletion of the widget instances.
 */
class RandomNumberWidget : AppWidgetProvider() {

    /**
     * Called when one or more instances of this App Widget need to be updated.
     * This method is responsible for updating the visual appearance of the widget.
     *
     * @param context The context in which the receiver is running.
     * @param appWidgetManager The AppWidgetManager object responsible for managing App Widget instances.
     * @param appWidgetIds The array of widget IDs for which an update is needed.
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    /**
     * Called when the first instance of this App Widget is created.
     *
     * @param context The context in which the receiver is running.
     */
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    /**
     * Called when the last instance of this App Widget is deleted.
     *
     * @param context The context in which the receiver is running.
     */
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Called when a broadcast Intent is received by the App Widget, specifically to handle widget click actions.
     *
     * This function intercepts broadcast intents and handles widget click actions. When the widget is clicked,
     * it generates a new random number and updates the widget's appearance with the new number.
     *
     * @param context The context in which the receiver is running.
     * @param intent The Intent being received.
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        // Check if the received intent is for the widget click action
        if (intent.action == WIDGET_CLICK) {
            // Retrieve the AppWidgetManager for updating App Widget instances
            val appWidgetManager = AppWidgetManager.getInstance(context)
            // Retrieve the RemoteViews for the widget layout
            val views = RemoteViews(context.packageName, R.layout.random_number_widget)
            // Generate a new random number
            val lastUpdate = "Random: " + NumberGenerator.generate(100)
            // Retrieve the App Widget ID from the intent
            val appWidgetId = intent.getIntExtra(WIDGET_ID_EXTRA, 0)
            // Update the TextView in the widget layout with the new random number
            views.setTextViewText(R.id.appwidget_text, lastUpdate)
            // Instruct the widget manager to update the widget with the new RemoteViews
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    companion object {
        const val WIDGET_CLICK = "android.appwidget.action.APPWIDGET_UPDATE"
        const val WIDGET_ID_EXTRA = "widget_id_extra"
    }
}

/**
 * Updates the App Widget with the latest random number and sets up a pending intent for a button click action.
 *
 * This function is responsible for updating the appearance of the App Widget with the latest random number
 * and setting up a pending intent for handling button click actions.
 *
 * @param context The context in which the receiver is running.
 * @param appWidgetManager The AppWidgetManager object responsible for managing App Widget instances.
 * @param appWidgetId The ID of the App Widget to be updated.
 */
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Prepare an intent for the button click action
    val intent = Intent(context, RandomNumberWidget::class.java)
    intent.action = RandomNumberWidget.WIDGET_CLICK
    intent.putExtra(RandomNumberWidget.WIDGET_ID_EXTRA, appWidgetId)

    // Create a pending intent for the button click action
    val pendingIntent = PendingIntent.getBroadcast(
        context, appWidgetId, intent,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            0
        }
    )

    // Generate a random number using the NumberGenerator utility class
    val lastUpdate = "Random: " + NumberGenerator.generate(100)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.random_number_widget)
    // Update the TextView in the widget layout with the generated random number
    views.setTextViewText(R.id.appwidget_text, lastUpdate)
    // Set the pending intent for the button click action
    views.setOnClickPendingIntent(R.id.btn_click, pendingIntent)

    // Instruct the widget manager to update the widget with the new RemoteViews
    appWidgetManager.updateAppWidget(appWidgetId, views)
}