package ml.clockvoid.gesturepack

import android.content.res.TypedArray
import android.os.Build
import android.support.v4.widget.NestedScrollView
import android.view.ViewGroup
import android.widget.ScrollView

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
