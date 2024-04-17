package com.kisahcode.androidintermediate

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

/**
 * Custom EditText class with a clear button functionality.
 *
 * This class extends the AppCompatEditText class and implements the View.OnTouchListener interface.
 * It provides a clear button on the right side of the EditText to clear its contents.
 *
 * @constructor Creates a MyEditText instance.
 * @param context The context in which the EditText is running.
 * @param attrs An AttributeSet that allows developers to configure the EditText in the XML layout.
 */
class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    // The clear button drawable.
    private var clearButtonImage: Drawable

    init {
        // Initialize the clear button drawable
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp) as Drawable

        // Set touch listener for the EditText
        setOnTouchListener(this)

        // Add text change listener to show or hide the clear button
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Show or hide the clear button based on text input
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    /**
     * Draws additional content, such as the clear button, within the EditText.
     *
     * @param canvas The Canvas on which to draw the content.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Set hint text for the EditText
        hint = "Masukkan nama Anda"

        // Set text alignment for the EditText
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    /**
     * Show the clear button.
     */
    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    /**
     * Hide the clear button.
     */
    private fun hideClearButton() {
        setButtonDrawables()
    }

    /**
     * Set drawables for the EditText.
     *
     * @param startOfTheText Drawable to be set at the start of the text.
     * @param topOfTheText Drawable to be set at the top of the text.
     * @param endOfTheText Drawable to be set at the end of the text.
     * @param bottomOfTheText Drawable to be set at the bottom of the text.
     */
    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText:Drawable? = null, endOfTheText:Drawable? = null, bottomOfTheText: Drawable? = null){
        // Set the Drawables to appear to the left of, above, to the right of, and below the text.
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    /**
     * Handles touch events on the EditText.
     *
     * @param v The view that received the touch event.
     * @param event The MotionEvent object containing full information about the event.
     * @return True if the event was handled, false otherwise.
     */
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        // Check if the clear button drawable is set
        if (compoundDrawables[2] != null) {
            // Variables to store the start and end positions of the clear button
            val clearButtonStart: Float
            val clearButtonEnd: Float
            // Flag to track if the clear button is clicked
            var isClearButtonClicked = false

            // Check the layout direction to determine clear button position
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                // Calculate clear button end position for right-to-left layout
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                // Check if touch event is within clear button bounds
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                // Calculate clear button start position for left-to-right layout
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                // Check if touch event is within clear button bounds
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }

            // Handle touch event if clear button is clicked
            if (isClearButtonClicked) {
                // Perform different actions based on touch event action
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Change clear button drawable on touch down
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        // Clear text on touch up
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false // Return false for other touch events
                }
            } else return false // Return false if clear button is not clicked
        }
        return false // Return false if clear button drawable is not set
    }
}