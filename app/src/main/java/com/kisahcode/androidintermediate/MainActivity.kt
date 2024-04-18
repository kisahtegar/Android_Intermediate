package com.kisahcode.androidintermediate

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding

/**
 * MainActivity of the Canvas Drawing application.
 *
 * This activity demonstrates how to draw on a canvas using various methods and techniques.
 */
class MainActivity : AppCompatActivity() {

    // View binding instance
    private lateinit var binding: ActivityMainBinding

    // Bitmap, Canvas, and Paint objects for drawing
    private val mBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
    private val mCanvas = Canvas(mBitmap)
    private val mPaint = Paint()

    // Constants for face dimensions
    private val left = 150F
    private val top = 250F
    private val right = mBitmap.width - left
    private val bottom = mBitmap.height.toFloat() - 50F

    // Constants for text and center of the canvas
    private val message = "Apakah kamu suka bermain?"
    private val halfOfWidth = (mBitmap.width/2).toFloat()
    private val halfOfHeight = (mBitmap.height/2).toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set bitmap to ImageView
        binding.imageView.setImageBitmap(mBitmap)

        // Show initial text on canvas
        showText()

        // Set click listeners for buttons
        binding.like.setOnClickListener {
            // Show a happy face
            showFace()
            showMouth(true)
            showEyes()
        }
        binding.dislike.setOnClickListener {
            // Show a sad face
            showFace()
            showMouth(false)
            showEyes()
        }
    }

    /**
     * Draws the face on the canvas.
     *
     * This function draws the face on the canvas by drawing two semicircles representing the left
     * and right sides of the face using the specified colors.
     */
    private fun showFace() {
        // Define a rectangle to represent the face
        val face = RectF(left, top, right, bottom)

        // Set the paint color to the left skin color
        mPaint.color = ResourcesCompat.getColor(resources, R.color.yellow_left_skin, null)

        // Draw a semicircle representing the left side of the face
        // The parameters are:
        // - the rectangle defining the bounds of the arc (the face)
        // - the start angle of the arc (90 degrees, which is the top of the circle)
        // - the sweep angle of the arc (180 degrees, which is half of a circle)
        // - a flag indicating whether to draw the arc clockwise (false)
        // - the paint used for drawing
        mCanvas.drawArc(face, 90F, 180F, false, mPaint)

        // Set the paint color to the right skin color
        mPaint.color = ResourcesCompat.getColor(resources, R.color.yellow_right_skin, null)

        // Draw a semicircle representing the right side of the face
        // The parameters are similar to the previous drawArc() call
        // The start angle is 270 degrees, which is the bottom of the circle
        mCanvas.drawArc(face, 270F, 180F, false, mPaint)

    }

    /**
     * Draws the eyes on the canvas.
     *
     * This function draws two eyes on the canvas using circles with specified colors and positions.
     * The eyes consist of black circles for pupils and white circles for highlights.
     */
    private fun showEyes() {
        // Set the paint color to black for drawing pupils
        mPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        // Draw the left eye's pupil as a black circle
        // The parameters are:
        // - the x-coordinate of the center of the circle (left eye)
        // - the y-coordinate of the center of the circle (left eye)
        // - the radius of the circle (50F)
        // - the paint used for drawing
        mCanvas.drawCircle(halfOfWidth - 100F, halfOfHeight - 10F, 50F, mPaint)
        // Draw the right eye's pupil as a black circle (similar parameters to the previous drawCircle() call)
        mCanvas.drawCircle(halfOfWidth + 100F, halfOfHeight - 10F, 50F, mPaint)

        // Set the paint color to white for drawing highlights
        mPaint.color = ResourcesCompat.getColor(resources, R.color.white, null)
        // Draw the left eye's highlight as a white circle
        // The parameters are:
        // - the x-coordinate of the center of the circle (left eye)
        // - the y-coordinate of the center of the circle (left eye)
        // - the radius of the circle (15F, smaller than the pupil)
        // - the paint used for drawing
        mCanvas.drawCircle(halfOfWidth - 120F, halfOfHeight - 20F, 15F, mPaint)
        // Draw the right eye's highlight as a white circle (similar parameters to the previous drawCircle() call)
        mCanvas.drawCircle(halfOfWidth + 80F, halfOfHeight - 20F, 15F, mPaint)
    }

    /**
     * Draws the mouth on the canvas.
     *
     * This function draws the mouth on the canvas based on the happiness state.
     * If the 'isHappy' parameter is true, it draws a smiling mouth. Otherwise, it draws a frowning mouth.
     *
     * @param isHappy Boolean indicating whether the mouth should be drawn in a smiling state.
     */
    private fun showMouth(isHappy: Boolean) {
        when (isHappy) {
            true -> {
                // Set the paint color to black for drawing the lip outline (smile)
                mPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
                // Define the bounding rectangle for the lip (smile)
                val lip = RectF(halfOfWidth - 200F, halfOfHeight - 100F, halfOfWidth + 200F, halfOfHeight + 400F)
                // Draw the lip (smile) as an arc
                // The parameters are:
                // - the rectangular bounds of the oval containing the arc
                // - the start angle of the arc (25 degrees, starting from the right side)
                // - the sweep angle of the arc (130 degrees, extending clockwise)
                // - a flag indicating whether the starting and ending angles should be connected by a straight line (false)
                // - the paint used for drawing
                mCanvas.drawArc(lip, 25F, 130F, false, mPaint)

                // Set the paint color to white for drawing the teeth
                mPaint.color = ResourcesCompat.getColor(resources, R.color.white, null)
                // Define the bounding rectangle for the mouth (smile)
                val mouth = RectF(halfOfWidth - 180F, halfOfHeight, halfOfWidth + 180F, halfOfHeight + 380F)
                // Draw the mouth (smile) as an arc (similar parameters to the previous drawArc() call)
                mCanvas.drawArc(mouth, 25F, 130F, false, mPaint)
            }
            false -> {
                // Set the paint color to black for drawing the lip outline (frown)
                mPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
                // Define the bounding rectangle for the lip (frown)
                val lip = RectF(halfOfWidth - 200F, halfOfHeight + 250F, halfOfWidth + 200F, halfOfHeight + 350F)
                // Draw the lip (frown) as an arc (similar parameters to the previous drawArc() call)
                mCanvas.drawArc(lip, 0F, -180F, false, mPaint)

                // Set the paint color to white for drawing the teeth
                mPaint.color = ResourcesCompat.getColor(resources, R.color.white, null)
                // Define the bounding rectangle for the mouth (frown)
                val mouth = RectF(halfOfWidth - 180F, halfOfHeight + 260F, halfOfWidth + 180F, halfOfHeight + 330F)
                // Draw the mouth (frown) as an arc (similar parameters to the previous drawArc() call)
                mCanvas.drawArc(mouth, 0F, -180F, false, mPaint)
            }
        }
    }

    /**
     * Draws text on the canvas.
     *
     * This function draws the specified message on the canvas at a calculated position.
     */
    private fun showText() {
        // Create a Paint object for drawing text with the FAKE_BOLD_TEXT_FLAG attribute
        val mPaintText =  Paint(Paint.FAKE_BOLD_TEXT_FLAG).apply {
            // Set the text size to 50F
            textSize = 50F
            // Set the text color to black
            color = ResourcesCompat.getColor(resources, R.color.black , null)
        }

        // Create a Rect object to hold the bounds of the text
        val mBounds = Rect()
        // Get the bounds of the message text using the Paint object
        mPaintText.getTextBounds(message, 0, message.length, mBounds)

        // Calculate the x-coordinate for drawing the text
        // The center of the canvas is calculated as halfOfWidth, and the center of the text is calculated as mBounds.centerX().
        // So, the x-coordinate for drawing the text is the difference between these two values.
        val x: Float = halfOfWidth - mBounds.centerX()
        // Set the y-coordinate for drawing the text to 50F
        val y = 50F
        // Draw the message text on the canvas at the calculated position (x, y) using the Paint object
        mCanvas.drawText(message, x, y, mPaintText)
    }
}