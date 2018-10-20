package ml.clockvoid.gesturepack

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import ml.clockvoid.gesturepack.R.attr.dragDismissFraction
import kotlin.math.log10

class Delegate(val mViewGroup: ViewGroup) {

    private var draggingDown: Boolean = false
    private var draggingUp: Boolean = false
    private var totalDrag: Int = 0
    private var dragElacticity: Float = 0.8f
    private var dragDismissScale: Float = 1f
    private var scrollStartTimestamp: Long = 0L
    private var dragDismissDistance: Float = Float.MAX_VALUE

    fun init(context: Context, a: TypedArray) {
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
            dragElacticity = a.getFloat(R.styleable.DraggableFrameLayout_dragElasticity,
                dragElacticity);
        }
    }

    fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (draggingDown && dy > 0 || draggingUp && dy < 0) {
            dragScale(dy)
            consumed[1] = dy
        }
    }

    fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        scrollStartTimestamp = System.currentTimeMillis()
        return (nestedScrollAxes and SCROLL_AXIS_VERTICAL) != 0
    }

    fun onNestedScroll(taerget: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        dragScale(dyUnconsumed)
    }

    private fun dragScale(scroll: Int) {
        if (scroll == 0) return
        totalDrag += scroll

        // track the direction & set the pivot point for scaling
        // don't double track i.e. if start dragging down and then reverse, keep tracking as
        // dragging down until they reach the 'natural' position
        if (scroll < 0 && !draggingUp && !draggingDown) {
            draggingDown = true
        } else if (scroll > 0 && !draggingDown && !draggingUp) {
            draggingUp = true
        }

        // how far have we dragged relative to the distance to perform a dismiss
        // (0–1 where 1 = dismiss distance). Decreasing logarithmically as we approach the limit
        val dragFraction: Float = log10(1 + (Math.abs(totalDrag) / dragDismissDistance))

        // calculate the desired translation given the drag fraction
        var dragTo: Float = dragFraction * dragDismissDistance * dragElacticity

        if (draggingUp) {
            // as we use the absolute magnitude when calculating the drag fraction, need to
            // re-apply the drag direction
            dragTo *= -1;
        }
        mViewGroup.translationY = dragTo
    }

    companion object {
        //copied from View in API 21
        const val SCROLL_AXIS_VERTICAL: Int = 1 shl 1
    }
}

object Util {

    fun checkParent(viewGroup: ViewGroup, a: TypedArray) {
        var checkParent = true
        if (a.hasValue(R.styleable.DraggableFrameLayout_ignoreNestedScrollWarnings)) {
            checkParent = a.getBoolean(R.styleable.DraggableFrameLayout_ignoreNestedScrollWarnings, false)
        }
        if (!checkParent) {
            return
        }

        if (viewGroup.parent is NestedScrollView) {
            if (!(viewGroup.parent as NestedScrollView).isNestedScrollingEnabled) {
                throw IllegalStateException("You need to set nestedScrollingEnabled on the NestedScrollView")
            }
        } else if (viewGroup.parent is ScrollView) {
            if (Build.VERSION.SDK_INT >= 21) {
                if (!(viewGroup.parent as ScrollView).isNestedScrollingEnabled) {
                    throw IllegalStateException("You need to set nestedScrollingEnabled on the ScrollView")

                }
            }
        }
    }
}
