package ml.clockvoid.gesturepack

import android.content.Context
import android.content.res.TypedArray
import android.view.View
import android.view.ViewGroup
import ml.clockvoid.gesturepack.R.attr.dragDismissFraction
import kotlin.math.log10

class HorizontalDelegate(private val mViewGroup: ViewGroup) : Delegate {
    override fun addListener(callback: Callback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dispatchDragCallback(
        elasticOffset: Float,
        elasticOffsetPixels: Float,
        rawOffset: Float,
        rawOffsetPixels: Float
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dispatchDismissCallback() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var draggingRight: Boolean = false
    private var draggingLeft: Boolean = false
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
        if (draggingRight && dx > 0 || draggingLeft && dx < 0) {
            dragScale(dx)
            consumed[1] = dx
        }
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        scrollStartTimestamp = System.currentTimeMillis()
        return (nestedScrollAxes and Delegate.SCROLL_AXIS_HORIZONTAL) != 0
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        dragScale(dxUnconsumed)
    }

    override fun dragScale(scroll: Int) {
        if (scroll == 0) return
        totalDrag += scroll

        // track the direction & set the pivot point for scaling
        // don't double track i.e. if start dragging down and then reverse, keep tracking as
        // dragging down until they reach the 'natural' position
        if (scroll < 0 && !draggingLeft && !draggingRight) {
            draggingRight = true
        } else if (scroll > 0 && !draggingRight && !draggingLeft) {
            draggingLeft = true
        }

        // how far have we dragged relative to the distance to perform a dismiss
        // (0–1 where 1 = dismiss distance). Decreasing logarithmically as we approach the limit
        val dragFraction: Float = log10(1 + (Math.abs(totalDrag) / dragDismissDistance))

        // calculate the desired translation given the drag fraction
        var dragTo: Float = dragFraction * dragDismissDistance * dragElasticity

        if (draggingLeft) {
            // as we use the absolute magnitude when calculating the drag fraction, need to
            // re-apply the drag direction
            dragTo *= -1
        }
        mViewGroup.translationX = dragTo
    }

    override fun onStopNestedScroll(child: View?) {
        // if the drag is too fast it probably was not an intentional drag but a fling, don't dismiss
        totalDrag = 0
        mViewGroup.translationX = totalDrag.toFloat()
        draggingRight = false
        draggingLeft = false
    }
}
