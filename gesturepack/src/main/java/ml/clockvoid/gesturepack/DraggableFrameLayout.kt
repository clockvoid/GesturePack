package ml.clockvoid.gesturepack

import android.content.Context
import android.content.res.TypedArray
import android.support.v4.view.NestedScrollingParent
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout

abstract class DraggableFrameLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(
    context,
    attrs,
    defStyleAttr
), NestedScrollingParent {

    lateinit var delegate: Delegate

    open fun init(context: Context, attrs: AttributeSet?) {
        Log.d("init", "init called")
        val a: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.VerticalDraggableFrameLayout, 0, 0)
        delegate.init(context, a)
        a.recycle()
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        Log.d("startscroll", "started")
        return delegate.onStartNestedScroll(child, target, nestedScrollAxes)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        Log.d("prescroll", "scrolling")
        delegate.onNestedPreScroll(target, dx, dy, consumed)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        Log.d("scroll", "scrolling")
        delegate.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    override fun onStopNestedScroll(p0: View) {
        Log.d("stopscroll", "stopscrolling")
        delegate.onStopNestedScroll(p0)
    }

    open fun addListener(callback: Callback) {
        delegate.addListener(callback)
    }
}