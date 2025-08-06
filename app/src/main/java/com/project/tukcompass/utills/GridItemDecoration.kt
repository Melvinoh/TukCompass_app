package com.project.tukcompass.utills

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint



import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(
    private val columnCount: Int,
    private val lineWidth: Int = 1,
    private val lineColor: Int = Color.LTGRAY
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = lineColor
        strokeWidth = lineWidth.toFloat()
        style = Paint.Style.STROKE
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = child.left - params.leftMargin
            val top = child.top - params.topMargin
            val right = child.right + params.rightMargin
            val bottom = child.bottom + params.bottomMargin

            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }
}
