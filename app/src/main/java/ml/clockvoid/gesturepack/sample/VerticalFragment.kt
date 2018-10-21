package ml.clockvoid.gesturepack.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.vertical_fragment.view.*
import ml.clockvoid.gesturepack.Callback

class VerticalFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.vertical_fragment, container, false)

        view.draggable_frame.addListener(object : Callback {
            override fun onDragDismissed() {
                fragmentManager?.popBackStack()
            }
        })

        return view
    }

}