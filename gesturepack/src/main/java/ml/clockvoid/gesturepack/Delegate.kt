package ml.clockvoid.gesturepack

import android.content.Context
import android.content.res.TypedArray
import android.view.View

interface Delegate {

    fun init(context: Context, a: TypedArray)

    fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray)

    fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean

    fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int)

    fun dragScale(scroll: Int)

    fun onStopNestedScroll(child: View?)

    companion object {
        //copied from View in API 21
        const val SCROLL_AXIS_VERTICAL: Int = 1 shl 1
        const val SCROLL_AXIS_HOIRIZONTAL: Int = 1
    }
}
