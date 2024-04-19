package com.kisahcode.androidintermediate

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
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
}

/**
 * Updates the App Widget with the latest random number.
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
    // Generate a random number using the NumberGenerator utility class
    val lastUpdate = "Random: " + NumberGenerator.generate(100)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.random_number_widget)
    views.setTextViewText(R.id.appwidget_text, lastUpdate)

    // Instruct the widget manager to update the widget with the new RemoteViews
    appWidgetManager.updateAppWidget(appWidgetId, views)
}