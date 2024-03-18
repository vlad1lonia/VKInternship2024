package com.application.vkinternship2024

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

class ArrowClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var hourPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 12f
        strokeCap = Paint.Cap.ROUND
    }

    private var minutePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 8f
        strokeCap = Paint.Cap.ROUND
    }

    private var secondPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 4f
        strokeCap = Paint.Cap.ROUND
    }

    private var divisionPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 16f
        strokeCap = Paint.Cap.ROUND
    }

    private var numberPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 86f
        color = Color.GRAY
        typeface = resources.getFont(R.font.madimi_one)
    }

    // Optional: Define paint for the outer circle
    private var outerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY // Choose color appropriate for your design
        style = Paint.Style.STROKE
        strokeWidth = 16f // Adjust the stroke width as needed
    }

    private var time = Calendar.getInstance()

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            time = Calendar.getInstance() // Update the current time
            invalidate() // Redraw the view
            handler.postDelayed(this, 10) // Schedule the next update in 1 second
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawClockFace(canvas)
        drawClockHands(canvas)
    }

    private fun drawClockHands(canvas: Canvas) {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width.coerceAtMost(height) / 3f

        val hour = time.get(Calendar.HOUR_OF_DAY) % 12
        val minute = time.get(Calendar.MINUTE)
        val second = time.get(Calendar.SECOND)
        val millisecond = time.get(Calendar.MILLISECOND)

        // Draw hour hand
        val hourAngle = Math.toRadians((hour + minute / 60.0) * 30 - 90).toFloat()
        val hourHandLength = radius * 0.56
        canvas.drawLine(centerX, centerY,
            (centerX + hourHandLength * cos(hourAngle.toDouble()).toFloat()).toFloat(),
            (centerY + hourHandLength * sin(hourAngle.toDouble()).toFloat()).toFloat(),
            hourPaint
        )

        // Draw minute hand
        val minuteAngle = Math.toRadians((minute + second / 60.0) * 6 - 90).toFloat()
        val minuteHandLength = radius * 0.72
        canvas.drawLine(centerX, centerY,
            (centerX + minuteHandLength * cos(minuteAngle.toDouble()).toFloat()).toFloat(),
            (centerY + minuteHandLength * sin(minuteAngle.toDouble()).toFloat()).toFloat(),
            minutePaint
        )

        // Draw second hand
        val secondAngle = Math.toRadians(((second + millisecond.toDouble() / 1000) * 6 - 90)).toFloat()
        val secondHandLength = radius * 0.92
        canvas.drawLine(centerX, centerY,
            (centerX + secondHandLength * cos(secondAngle)).toFloat(),
            (centerY + secondHandLength * sin(secondAngle)).toFloat(),
            secondPaint
        )

        val centerDotRadius = 6
        canvas.drawCircle(centerX, centerY, centerDotRadius.toFloat(), outerCirclePaint)
    }

    private fun drawClockFace(canvas: Canvas) {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width.coerceAtMost(height) / 2f * 0.8f // Slightly reduce radius for aesthetics
        val outerCircleRadius = radius + 20

        // Draw the outer circle
        canvas.drawCircle(centerX, centerY, outerCircleRadius, outerCirclePaint)

        // Draw hour markers and numbers
        for (i in 1..12) {
            val angle = Math.toRadians((i * 30 - 90).toDouble()).toFloat()
            val startX = (centerX + cos(angle.toDouble()) * radius).toFloat()
            val startY = (centerY + sin(angle.toDouble()) * radius).toFloat()
            val stopX = (centerX + cos(angle.toDouble()) * (radius - 40)).toFloat()
            val stopY = (centerY + sin(angle.toDouble()) * (radius - 40)).toFloat()

            canvas.drawLine(startX, startY, stopX, stopY, divisionPaint)
            canvas.drawLine(startX, startY, stopX, stopY, divisionPaint)

            val number = i.toString()
            val textWidth = numberPaint.measureText(number)
            val textHeight = numberPaint.descent() - numberPaint.ascent()

            val xPaddings = listOf<Double>(
                -1.0,
                textWidth.toDouble() * .5 * (4.0 / 3), textWidth.toDouble() * .5 * (5.0 / 3), textWidth.toDouble() * .5 * (6.0 / 3),
                textWidth.toDouble() * .5 * (5.0 / 3), textWidth.toDouble() * .5 * (4.0 / 3), 0.0,
                -textWidth.toDouble() * .5 * (4.0 / 3), -textWidth.toDouble() * .5 * (5.0 / 3), -textWidth.toDouble() * .5 * (6.0 / 3),
                -textWidth.toDouble() * .5 * (5.0 / 3), -textWidth.toDouble() * .5 * (4.0 / 3), 0.0
            )

            val yPaddings = listOf<Double>(
                -1.0,
                -textHeight.toDouble() * .5 * (5.0 / 3), -textHeight.toDouble() * .5 * (4.0 / 3), -textHeight.toDouble() * .5 * (3.0 / 3),
                -textHeight.toDouble() * .5 * (2.0 / 3), -textHeight.toDouble() * .5 * (1.0 / 3), 0.0,
                -textHeight.toDouble() * .5 * (1.0 / 3), -textHeight.toDouble() * .5 * (2.0 / 3), -textHeight.toDouble() * .5 * (3.0 / 3),
                -textHeight.toDouble() * .5 * (4.0 / 3), -textHeight.toDouble() * .5 * (5.0 / 3), -textHeight.toDouble() * .5 * (6.0 / 3)
            )

            canvas.drawText(number, (stopX - textWidth / 2 - xPaddings[i]).toFloat(),
                (stopY - textHeight / 4 - yPaddings[i]).toFloat(), numberPaint)
        }

        // Optionally, draw minute markers
        for (i in 1..60) {
            if (i % 5 != 0) { // Skip hour markers positions
                val angle = Math.toRadians((i * 6 - 90).toDouble()).toFloat()
                val startX = (centerX + cos(angle.toDouble()) * radius).toFloat()
                val startY = (centerY + sin(angle.toDouble()) * radius).toFloat()
                val stopX = (centerX + cos(angle.toDouble()) * (radius - 10)).toFloat()
                val stopY = (centerY + sin(angle.toDouble()) * (radius - 10)).toFloat()

                canvas.drawLine(startX, startY, stopX, stopY, divisionPaint)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.post(updateRunnable) // Start updates when the view is attached to the window
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(updateRunnable) // Stop updates to prevent memory leaks
    }
}
