package com.kisahcode.androidintermediate

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import java.util.*

/**
 * Factory class for providing views for the StackWidget.
 *
 * This class implements the RemoteViewsService.RemoteViewsFactory interface to provide
 * data and views to the StackWidget. It manages the data items to be displayed in the
 * widget and creates RemoteViews for each item.
 *
 * @param mContext The context in which the factory is operating.
 */
internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    // List to hold the widget items (Bitmaps)
    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onCreate() {
        // Initialization code can be added here if needed
    }

    override fun onDataSetChanged() {
        // This method is called to refresh the widget when there are changes
        // Here, we add Bitmaps representing widget items to mWidgetItems
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.darth_vader))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.star_wars_logo))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.storm_trooper))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.starwars))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.falcon))
    }

    override fun onDestroy() {
        // Cleanup code can be added here if needed
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        // This method is responsible for creating and returning RemoteViews for each item in the widget
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        // Set a fill-in intent for the item
        val extras = bundleOf(
            ImagesBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}