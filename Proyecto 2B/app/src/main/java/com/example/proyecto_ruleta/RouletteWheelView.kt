package com.example.proyecto_ruleta

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.min

class RouletteWheelView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var options = mutableListOf<String>()
    private var sweepAngles = mutableListOf<Float>()
    private var rotationAngle = 0f

    fun setupWheelOptions(newOptions: List<String>) { // 👈 Nombre único
        this.options = newOptions.toMutableList()
        calculateAngles()
        invalidate()
    }

    private fun calculateAngles() {
        sweepAngles.clear()
        val anglePerSection = 360f / options.size
        repeat(options.size) { sweepAngles.add(anglePerSection) }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = (min(width, height) / 2 * 0.8).toFloat()
        val centerX = width / 2f
        val centerY = height / 2f

        var startAngle = rotationAngle
        for ((index, angle) in sweepAngles.withIndex()) {
            paint.color = getColorForIndex(index)
            canvas.drawArc(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                startAngle,
                angle,
                true,
                paint
            )

            // Dibujar texto
            drawTextOnArc(canvas, options[index], centerX, centerY, radius, startAngle, angle)
            startAngle += angle
        }
    }

    private fun drawTextOnArc(canvas: Canvas, text: String, cx: Float, cy: Float, radius: Float, startAngle: Float, sweepAngle: Float) {
        val path = Path()
        path.addArc(
            cx - radius,
            cy - radius,
            cx + radius,
            cy + radius,
            startAngle,
            sweepAngle
        )

        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 24f
            textAlign = Paint.Align.CENTER
        }

        canvas.drawTextOnPath(text, path, radius * 0.5f, radius * 0.5f, textPaint)
    }

    fun rotateWheel(targetAngle: Float, duration: Long = 3000) {
        val animator = ValueAnimator.ofFloat(0f, targetAngle)
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            rotationAngle = it.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    private fun getColorForIndex(index: Int): Int {
        val colors = listOf(
            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA
        )
        return colors[index % colors.size]
    }
}