package com.kisahcode.androidintermediate

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import androidx.core.content.res.ResourcesCompat

/**
 * Custom view for displaying and selecting seats.
 *
 * This view represents a seating arrangement with interactive seat selection.
 * Seats can be displayed and selected using touch input.
 *
 * @constructor Creates a SeatsView instance.
 * @param context The context in which the view is running.
 * @param attrs An AttributeSet that allows developers to configure the view in the XML layout.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource
 * to apply to this view. If 0, no default style will be applied.
 */
class SeatsView : View {

    // Paint objects for drawing seats and text
    private val backgroundPaint = Paint()
    private val armrestPaint = Paint()
    private val bottomSeatPaint = Paint()
    private val numberSeatPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
    private val titlePaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    // Bounds for text measurements
    private val mBounds = Rect()

    // List of seats
    private val seats: ArrayList<Seat> = arrayListOf(
        Seat(id = 1, name = "A1", isBooked = false),
        Seat(id = 2, name = "A2", isBooked = false),
        Seat(id = 3, name = "B1", isBooked = false),
        Seat(id = 4, name = "A4", isBooked = false),
        Seat(id = 5, name = "C1", isBooked = false),
        Seat(id = 6, name = "C2", isBooked = false),
        Seat(id = 7, name = "D1", isBooked = false),
        Seat(id = 8, name = "D2", isBooked = false),
    )
    // Currently selected seat
    var seat: Seat? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * Measure the view and its content to determine the measured width and the measured height.
     *
     * This method is called to find out how big a view should be. The parent supplies constraint
     * information in the form of width and height constraints. The job of this method is to use
     * this information to determine how big the view should be and to set its size via the
     * `setMeasuredDimension` method.
     *
     * @param widthMeasureSpec Horizontal space requirements as imposed by the parent.
     * @param heightMeasureSpec Vertical space requirements as imposed by the parent.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Get the suggested minimum width and height based on constraints
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        // Calculate half of the height and width of the view
        val halfOfHeight = height / 2
        val halfOfWidth = width / 2

        // Initial value for positioning seats
        var value = -600F

        // Position seats based on the calculated coordinates
        for (i in 0..7) {
            if (i.mod(2) == 0) {
                // Even index seats are positioned to the left of the center
                seats[i].apply {
                    x = halfOfWidth - 300F
                    y = halfOfHeight + value
                }
            } else {
                // Odd index seats are positioned to the right of the center
                seats[i].apply {
                    x = halfOfWidth + 100F
                    y = halfOfHeight + value
                }
                // Increment the value for the next row of seats
                value += 300F
            }
        }
    }

    /**
     * Draw the view.
     *
     * This method is responsible for rendering the view on the provided canvas.
     * It iterates over each seat in the list and draws them using the `drawSeat` method.
     * Additionally, it draws the title text in the center of the view.
     *
     * @param canvas The Canvas on which the view is drawn.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw each seat
        for (seat in seats) {
            drawSeat(canvas, seat)
        }

        // Draw title text
        val text = "Silakan Pilih Kursi"
        titlePaint.apply {
            textSize = 50F
        }
        canvas.drawText(text, (width / 2F) - 197F, 100F, titlePaint)
    }

    /**
     * Draw a seat on the canvas.
     *
     * This method is responsible for drawing a single seat on the provided canvas.
     * It determines the color scheme based on whether the seat is booked or not.
     * The seat consists of a background shape, armrests, bottom part, and seat number.
     *
     * @param canvas The Canvas on which to draw the seat.
     * @param seat The Seat object representing the seat to be drawn.
     */
    private fun drawSeat(canvas: Canvas?, seat: Seat) {
        // Set colors based on whether the seat is booked or not
        if (seat.isBooked) {
            // Set colors for a booked seat
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        } else {
            // Set colors for an available seat
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.blue_500, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.blue_700, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.blue_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
        }

        // Save the current canvas state
        canvas?.save()

        // Draw background shape
        canvas?.translate(seat.x as Float, seat.y as Float)
        val backgroundPath = Path()
        backgroundPath.addRect(0F, 0F, 200F, 200F, Path.Direction.CCW)
        backgroundPath.addCircle(100F, 50F, 75F, Path.Direction.CCW)
        canvas?.drawPath(backgroundPath, backgroundPaint)

        // Draw armrests
        val armrestPath = Path()
        armrestPath.addRect(0F, 0F, 50F, 200F, Path.Direction.CCW)
        canvas?.drawPath(armrestPath, armrestPaint)
        canvas?.translate(150F, 0F)
        armrestPath.addRect(0F, 0F, 50F, 200F, Path.Direction.CCW)
        canvas?.drawPath(armrestPath, armrestPaint)

        // Draw bottom part of the seat
        canvas?.translate(-150F, 175F)
        val bottomSeatPath = Path()
        bottomSeatPath.addRect(0F, 0F, 200F, 25F, Path.Direction.CCW)
        canvas?.drawPath(bottomSeatPath, bottomSeatPaint)

        // Draw seat number
        canvas?.translate(0F, -175F)
        numberSeatPaint.apply {
            textSize = 50F
            // Measure the text bounds to center it properly
            numberSeatPaint.getTextBounds(seat.name, 0, seat.name.length, mBounds)
        }
        canvas?.drawText(seat.name, 100F - mBounds.centerX(), 100F, numberSeatPaint)

        // Restore the canvas state to the previous configuration
        canvas?.restore()
    }

    /**
     * Handle touch events on the view.
     *
     * This method is responsible for handling touch events such as touch down.
     * It detects which seat was touched based on touch coordinates and triggers
     * the booking process for that seat.
     *
     * @param event The MotionEvent object containing full information about the event.
     * @return True if the event was handled, false otherwise.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Calculate half of the height and width of the view
        val halfOfHeight = height / 2
        val halfOfWidth = width / 2

        // Define ranges for columns and rows
        val widthColumnOne = (halfOfWidth - 300F)..(halfOfWidth - 100F)
        val widthColumnTwo = (halfOfWidth + 100F)..(halfOfWidth + 300F)
        val heightRowOne = (halfOfHeight - 600F)..(halfOfHeight - 400F)
        val heightRowTwo = (halfOfHeight - 300F)..(halfOfHeight - 100F)
        val heightRowTree = (halfOfHeight + 0F)..(halfOfHeight + 200F)
        val heightRowFour =(halfOfHeight + 300F)..(halfOfHeight + 500F)

        // Handle touch down event
        if (event?.action == ACTION_DOWN) {
            // Determine which seat was touched based on touch coordinates
            when {
                event.x in widthColumnOne && event.y in heightRowOne -> booking(0)
                event.x in widthColumnTwo && event.y in heightRowOne -> booking(1)
                event.x in widthColumnOne && event.y in heightRowTwo -> booking(2)
                event.x in widthColumnTwo && event.y in heightRowTwo -> booking(3)
                event.x in widthColumnOne && event.y in heightRowTree -> booking(4)
                event.x in widthColumnTwo && event.y in heightRowTree -> booking(5)
                event.x in widthColumnOne && event.y in heightRowFour -> booking(6)
                event.x in widthColumnTwo && event.y in heightRowFour -> booking(7)
            }
        }
        return true
    }

    /**
     * Book a seat.
     *
     * This method is responsible for booking a seat based on its position in the list of seats.
     * It sets all seats as unbooked and then marks the selected seat as booked.
     * Finally, it invalidates the view to trigger a redraw.
     *
     * @param position The position of the seat to be booked in the list of seats.
     */
    private fun booking(position: Int) {
        // Set all seats as unbooked
        for (seat in seats) {
            seat.isBooked = false
        }

        // Mark the selected seat as booked
        seats[position].apply {
            seat = this
            isBooked = true
        }

        // Invalidate the view to trigger a redraw
        invalidate()
    }
}

/**
 * Represents a seat.
 *
 * This data class encapsulates the properties of a seat, including its unique identifier,
 * coordinates (x and y), name, and booking status.
 *
 * @property id The unique identifier of the seat.
 * @property x The x-coordinate of the seat's position.
 * @property y The y-coordinate of the seat's position.
 * @property name The name or label of the seat.
 * @property isBooked A boolean indicating whether the seat is booked or not.
 */
data class Seat(
    val id: Int,
    var x: Float? = 0F,
    var y: Float? = 0F,
    var name: String,
    var isBooked: Boolean
)