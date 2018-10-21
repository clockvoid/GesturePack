package ml.clockvoid.gesturepack

import android.content.Context
import android.content.res.TypedArray
import android.view.View
import android.view.ViewGroup
import ml.clockvoid.gesturepack.R.attr.dragDismissFraction

abstract class Delegate(protected val mViewGroup: ViewGroup) {

    protected var totalDrag: Int = 0
    protected var dragElasticity: Float = 0.8f
    protected var dragDismissScale: Float = 1f
    protected var scrollStartTimestamp: Long = 0L
    protected var dragDismissDistance: Float = Float.MAX_VALUE

    protected var callbacks: MutableList<Callback> = mutableListOf()

    open fun init(context: Context, a: TypedArray) {
        Util.checkParent(mViewGroup, a)

        if (a.hasValue(R.styleable.VerticalDraggableFrameLayout_dragDismissDistance)) {
            dragDismissDistance = a.getDimensionPixelSize(R.styleable
                .VerticalDraggableFrameLayout_dragDismissDistance, 0).toFloat()
        } else if (a.hasValue(R.styleable.VerticalDraggableFrameLayout_dragDismissFraction)) {
            dragDismissFraction = a.getFloat(R.styleable
                .VerticalDraggableFrameLayout_dragDismissFraction, dragDismissFraction.toFloat()
            ).toInt()
        }
        if (a.hasValue(R.styleable.VerticalDraggableFrameLayout_dragDismissScale)) {
            dragDismissScale = a.getFloat(R.styleable
                .VerticalDraggableFrameLayout_dragDismissScale, dragDismissScale)
        }
        if (a.hasValue(R.styleable.VerticalDraggableFrameLayout_dragElasticity)) {
            dragElasticity = a.getFloat(R.styleable.VerticalDraggableFrameLayout_dragElasticity,
                dragElasticity)
        }
    }

    open fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return false
    }

    open fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray){
    }

    open fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
    }

    open fun dragScale(scroll: Int) {
    }

    open fun onStopNestedScroll(child: View?) {
    }

    open fun addListener(callback: Callback) {
        callbacks.add(callback)
    }

    open fun dispatchDragCallback(elasticOffset: Float, elasticOffsetPixels: Float, rawOffset: Float, rawOffsetPixels: Float) {
        callbacks.forEach { callback ->
            callback.onDrag(elasticOffset, elasticOffsetPixels, rawOffset, rawOffsetPixels)
        }
    }

    open fun dispatchDismissCallback() {
        callbacks.forEach { callback ->
            callback.onDragDismissed()
        }
    }

    companion object {
        //copied from View in API 21
        const val SCROLL_AXIS_VERTICAL: Int = 1 shl 1
        const val SCROLL_AXIS_HORIZONTAL: Int = 1
        const val DRAG_DURATION_BUFFER: Long = 150L
    }
}
