package com.example.proyecto_ruleta

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.math.min
import kotlin.random.Random

class RouletteWheelView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var options = mutableListOf<String>()
    private var sweepAngles = mutableListOf<Float>()
    private var rotationAngle = 0f

    fun setupWheelOptions(newOptions: List<String>) { //
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

    private fun drawTextOnArc(
        canvas: Canvas,
        text: String,
        cx: Float,
        cy: Float,
        radius: Float,
        startAngle: Float,
        sweepAngle: Float
    ) {
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = radius * 0.12f  // Tamaño base ajustado
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        // Ajuste dinámico de tamaño de texto
        val maxTextWidth = (2 * Math.PI * radius * (sweepAngle / 360)).toFloat() * 0.7f
        var textSize = textPaint.textSize
        while (textPaint.measureText(text) > maxTextWidth && textSize > 12f) {
            textSize *= 0.9f
            textPaint.textSize = textSize
        }

        val path = Path().apply {
            addArc(
                cx - radius,
                cy - radius,
                cx + radius,
                cy + radius,
                startAngle + 2f,  // Offset para centrado visual
                sweepAngle - 4f    // Margen para bordes
            )
        }

        // Centrado vertical preciso
        val metrics = textPaint.fontMetrics
        val verticalOffset = -(metrics.ascent + metrics.descent) / 2

        canvas.drawTextOnPath(
            text.uppercase(),  // Texto en mayúsculas para mejor legibilidad
            path,
            0f,
            verticalOffset,
            textPaint
        )
    }

    fun rotateWheel(targetRotations: Float = 5F, duration: Long = 4000) {
        val random = Random
        val baseAngle = 360f * targetRotations
        val finalAngle = baseAngle + random.nextFloat() * 360

        val animator = ValueAnimator.ofFloat(0f, finalAngle).apply {
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                rotationAngle = it.animatedValue as Float
                invalidate()
            }
        }
        animator.start()
    }

    private fun getColorForIndex(index: Int): Int {
        val colors = listOf(
            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.GRAY, Color.DKGRAY, Color.LTGRAY
        )
        return colors[index % colors.size]
    }
}