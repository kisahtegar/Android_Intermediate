package com.kisahcode.androidintermediate

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

/**
 * Custom Button class with different backgrounds and text based on its enabled state.
 *
 * This class extends the AppCompatButton class and provides custom backgrounds and text color
 * based on whether the button is enabled or disabled.
 *
 * @constructor Creates a MyButton instance.
 * @param context The context in which the Button is running.
 * @param attrs An AttributeSet that allows developers to configure the Button in the XML layout.
 */
class MyButton : AppCompatButton {

    // Constructor for MyButton when used programmatically in Activity/Fragment
    constructor(context: Context) : super(context)

    // Constructor for MyButton when used in XML layout
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    // Text color for the button
    private var txtColor: Int = 0

    // Background drawables for enabled and disabled states
    private var enabledBackground: Drawable
    private var disabledBackground: Drawable

    // Initialize resources for MyButton
    init {
        // Initialize text color from resources
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)

        // Initialize background drawables for enabled and disabled states
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }

    /**
     * Customizes the appearance of the button based on its enabled state.
     *
     * @param canvas The Canvas on which to draw the content.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Set background drawable based on the enabled state
        background = if(isEnabled) enabledBackground else disabledBackground

        // Set text color
        setTextColor(txtColor)

        // Set text size
        textSize = 12f

        // Set gravity to center text within the button
        gravity = Gravity.CENTER

        // Set text based on the enabled state
        text = if(isEnabled) "Submit" else "Isi Dulu"
    }
}