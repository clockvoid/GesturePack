package ml.clockvoid.gesturepack

import android.util.Log
import android.view.View
import android.view.ViewGroup

class VerticalDelegate(mViewGroup: ViewGroup) : Delegate(mViewGroup) {

    private var draggingDown: Boolean = false
    private var draggingUp: Boolean = false

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (draggingDown && dy > 0 || draggingUp && dy < 0) {
            dragScale(dy)
            consumed[1] = dy
        }
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        scrollStartTimestamp = System.currentTimeMillis()
        Log.d("nestedScrollAxes", "$nestedScrollAxes")
        return (nestedScrollAxes and Delegate.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        dragScale(dyUnconsumed)
    }

    override fun dragScale(scroll: Int) {
        if (scroll == 0) return
        totalDrag -= scroll

        // track the direction & set the pivot point for scaling
        // don't double track i.e. if start dragging down and then reverse, keep tracking as
        // dragging down until they reach the 'natural' position
        if (scroll < 0 && !draggingUp && !draggingDown) {
            draggingDown = true
        } else if (scroll > 0 && !draggingDown && !draggingUp) {
            draggingUp = true
        }

        mViewGroup.translationY = totalDrag.toFloat()
    }

    override fun onStopNestedScroll(child: View) {
        // if the drag is too fast it probably was not an intentional drag but a fling, don't dismiss
        val dragTime: Long = System.currentTimeMillis() - scrollStartTimestamp
        if (dragTime > DRAG_DURATION_BUFFER) {
            dispatchDismissCallback()
        } else {
            totalDrag = 0
            mViewGroup.translationY = totalDrag.toFloat()
        }
        draggingDown = false
        draggingUp = false
    }
}
