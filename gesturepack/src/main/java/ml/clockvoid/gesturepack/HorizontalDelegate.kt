package ml.clockvoid.gesturepack

import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.View
import android.view.ViewGroup

class HorizontalDelegate(mViewGroup: ViewGroup) : Delegate(mViewGroup) {

    private var draggingRight: Boolean = false
    private var draggingLeft: Boolean = false

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (draggingRight && dx < 0 || draggingLeft && dx > 0) {
            dragScale(dx)
            consumed[0] = dx
        }
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        scrollStartTimestamp = System.currentTimeMillis()
        return (nestedScrollAxes and NestedScrollView.SCROLL_AXIS_HORIZONTAL) != 0
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        dragScale(dxUnconsumed)
    }

    override fun dragScale(scroll: Int) {
        if (scroll == 0) return
        totalDrag -= scroll

        // track the direction & set the pivot point for scaling
        // don't double track i.e. if start dragging down and then reverse, keep tracking as
        // dragging down until they reach the 'natural' position
        if (scroll < 0 && !draggingLeft && !draggingRight) {
            draggingRight = true
        } else if (scroll > 0 && !draggingRight && !draggingLeft) {
            draggingLeft = true
        }

        mViewGroup.translationX = totalDrag.toFloat()
    }

    override fun onStopNestedScroll(child: View?) {
        Log.d("stop scrolling", "when translationX is $totalDrag")
        // if the drag is too fast it probably was not an intentional drag but a fling, don't dismiss
        val dragTime:Long = System.currentTimeMillis() - scrollStartTimestamp
        if (dragTime > DRAG_DURATION_BUFFER) {
            dispatchDismissCallback()
        } else {
            totalDrag = 0
            mViewGroup.translationX = totalDrag.toFloat()
        }
        draggingRight = false
        draggingLeft = false
    }
}
