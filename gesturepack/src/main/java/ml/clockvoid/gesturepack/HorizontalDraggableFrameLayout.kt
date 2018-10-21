package ml.clockvoid.gesturepack

import android.content.Context
import android.util.AttributeSet

class HorizontalDraggableFrameLayout : DraggableFrameLayout {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        delegate = HorizontalDelegate(this)
        init(context, attrs)
    }
}
