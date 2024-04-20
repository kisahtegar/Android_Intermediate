package com.kisahcode.androidintermediate

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri

/**
 * Implementation of App Widget functionality for displaying a stack of images.
 *
 * This widget displays a stack of images that the user can scroll through horizontally.
 * Tapping on an image will display a toast message showing the index of the touched image.
 */
class ImagesBannerWidget : AppWidgetProvider() {

    /**
     * Called when the App Widget is updated.
     *
     * @param context The application context
     * @param appWidgetManager The AppWidgetManager instance
     * @param appWidgetIds The array of widget IDs
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
     * Called when the first instance of this widget is added to the home screen.
     *
     * @param context The application context
     */
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    /**
     * Called when the last instance of this widget is removed from the home screen.
     *
     * @param context The application context
     */
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Called when the widget receives a broadcast.
     *
     * @param context The application context
     * @param intent The received Intent
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action != null) {
            // Check if the received intent has the specified action
            if (intent.action == TOAST_ACTION) {
                // Display a toast message showing the index of the touched image
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TOAST_ACTION = "com.kisahcode.TOAST_ACTION"
        const val EXTRA_ITEM = "com.kisahcode.EXTRA_ITEM"

        /**
         * Update the App Widget UI.
         *
         * @param context The application context
         * @param appWidgetManager The AppWidgetManager instance
         * @param appWidgetId The ID of the widget to be updated
         */
        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            // Create an intent to start the StackWidgetService
            val intent = Intent(context, StackWidgetService::class.java)
            // Set the widget ID as an extra in the intent
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            // Convert the intent to a URI and set it as data for the intent
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            // Create RemoteViews for the widget layout
            val views = RemoteViews(context.packageName, R.layout.images_banner_widget)
            // Set the RemoteViews to use a RemoteViewsService (StackWidgetService)
            views.setRemoteAdapter(R.id.stack_view, intent)
            // Set the empty view
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            // Create an intent for handling the touch event on the images
            val toastIntent = Intent(context, ImagesBannerWidget::class.java)
            // Set the action for the intent
            toastIntent.action = TOAST_ACTION
            // Set the widget ID as an extra in the intent
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            // Create a PendingIntent for the touch event
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else 0
            )

            // Set the PendingIntent as a template for the touch event
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            // Update the App Widget UI
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}