package ml.clockvoid.gesturepack

import android.content.Context
import android.content.res.TypedArray
import android.view.View
import android.view.ViewGroup
import ml.clockvoid.gesturepack.R.attr.dragDismissFraction

class VerticalDelegate(private val mViewGroup: ViewGroup) : Delegate {

    private var draggingDown: Boolean = false
    private var draggingUp: Boolean = false
    private var totalDrag: Int = 0
    private var dragElasticity: Float = 0.8f
    private var dragDismissScale: Float = 1f
    private var scrollStartTimestamp: Long = 0L
    private var dragDismissDistance: Float = Float.MAX_VALUE

    override fun init(context: Context, a: TypedArray) {
        Util.checkParent(mViewGroup, a)

        if (a.hasValue(R.styleable.DraggableFrameLayout_dragDismissDistance)) {
            dragDismissDistance = a.getDimensionPixelSize(R.styleable
                .DraggableFrameLayout_dragDismissDistance, 0).toFloat()
        } else if (a.hasValue(R.styleable.DraggableFrameLayout_dragDismissFraction)) {
            dragDismissFraction = a.getFloat(R.styleable
                .DraggableFrameLayout_dragDismissFraction, dragDismissFraction.toFloat()
            ).toInt()
        }
        if (a.hasValue(R.styleable.DraggableFrameLayout_dragDismissScale)) {
            dragDismissScale = a.getFloat(R.styleable
                .DraggableFrameLayout_dragDismissScale, dragDismissScale)
        }
        if (a.hasValue(R.styleable.DraggableFrameLayout_dragElasticity)) {
            dragElasticity = a.getFloat(R.styleable.DraggableFrameLayout_dragElasticity,
                dragElasticity)
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (draggingDown && dy > 0 || draggingUp && dy < 0) {
            dragScale(dy)
            consumed[1] = dy
        }
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        scrollStartTimestamp = System.currentTimeMillis()
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

    override fun onStopNestedScroll(child: View?) {
        // if the drag is too fast it probably was not an intentional drag but a fling, don't dismiss
        totalDrag = 0
        mViewGroup.translationY = totalDrag.toFloat()
        draggingDown = false
        draggingUp = false
    }
}