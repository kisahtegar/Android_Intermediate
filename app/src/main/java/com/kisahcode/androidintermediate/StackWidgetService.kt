package com.kisahcode.androidintermediate

import android.content.Intent
import android.widget.RemoteViewsService

/**
 * A service to provide factory for the RemoteViews to be displayed in the StackWidget.
 */
class StackWidgetService : RemoteViewsService() {

    /**
     * Called when RemoteViewsFactory is requested by the AppWidgetManager.
     *
     * @param intent The intent sent by the AppWidgetManager.
     * @return RemoteViewsFactory instance for the StackWidget.
     */
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}