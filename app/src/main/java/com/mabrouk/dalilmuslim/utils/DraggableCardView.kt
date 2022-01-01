package com.mabrouk.dalilmuslim.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import com.mabrouk.dalilmuslim.utils.Draggable.DRAG_TOLERANCE
import java.lang.Float.max
import java.lang.Float.min
import java.lang.Math.abs

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 28/08/2021 created by Just clean
 */
class DraggableCardView(context: Context, attrs: AttributeSet) :
    CardView(context, attrs) {

    private var draggableListener: DraggableListener? = null
    private var widgetInitialX: Float = 0F
    private var widgetDX: Float = 0F
    private var widgetInitialY: Float = 0F
    private var widgetDY: Float = 0F

    init {
        draggableSetup()
    }

    @SuppressLint("NewApi")
    private fun draggableSetup() {
        this.setOnTouchListener { v, event ->
            val viewParent = v.parent as View
            val parentHeight = viewParent.height
            val parentWidth = viewParent.width
            val xMax = parentWidth - v.width
            val xMiddle = parentWidth / 2
            val yMax = parentHeight - v.height

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    widgetDX = v.x - event.rawX
                    widgetDY = v.y - event.rawY
                    widgetInitialX = v.x
                    widgetInitialY = v.y
                }
                MotionEvent.ACTION_MOVE -> {
                    var newX = event.rawX + widgetDX
                    newX = max(0F, newX)
                    newX = min(xMax.toFloat(), newX)
                    v.x = newX

                    var newY = event.rawY + widgetDY
                    newY = max(0F, newY)
                    newY = min(yMax.toFloat(), newY)
                    v.y = newY

                    draggableListener?.onPositionChanged(v)
                }
                MotionEvent.ACTION_UP -> {
                    if (event.rawX >= xMiddle) {
                        v.animate().x(xMax.toFloat())
                            .setDuration(Draggable.DURATION_MILLIS)
                            .setUpdateListener { draggableListener?.onPositionChanged(v) }
                            .start()
                    } else {
                        v.animate().x(0F).setDuration(Draggable.DURATION_MILLIS)
                            .setUpdateListener { draggableListener?.onPositionChanged(v) }
                            .start()
                    }
                    if (kotlin.math.abs(v.x - widgetInitialX) <= DRAG_TOLERANCE && kotlin.math.abs(v.y - widgetInitialY) <= DRAG_TOLERANCE) {
                        performClick()
                    } else draggableListener?.xAxisChanged(event.rawX >= xMiddle)
                }
                else -> return@setOnTouchListener false
            }
            true
        }
    }

    override fun performClick(): Boolean {
        Log.d("DraggableImageView", "click")
        return super.performClick()
    }

    fun setListener(draggableListener: DraggableListener?) {
        this.draggableListener = draggableListener
    }
}

object Draggable {
    const val DRAG_TOLERANCE = 16
    const val DURATION_MILLIS = 250L
}

interface DraggableListener {
    fun onPositionChanged(view: View)
    fun xAxisChanged(isInRightSide: Boolean)
}