package ml.clockvoid.gesturepack.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.horizontal_fragment.view.*
import ml.clockvoid.gesturepack.Callback

class HorizontalFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.horizontal_fragment, container, false)

        view.draggable_frame.addListener(object : Callback {
            override fun onDragDismissed() {
                fragmentManager?.popBackStack()
            }
        })

        val layoutManager
                = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        view.recyvlerview.layoutManager = layoutManager
        view.recyvlerview.adapter = Adapter()

        return view
    }

    companion object {
        class Adapter : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
                return ViewHolder.inflate(p0)
            }

            override fun getItemCount(): Int {
                return 1
            }

            override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            }

        }

        class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            companion object {
                fun inflate(parent: ViewGroup): ViewHolder =
                    ViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.horizontal_recycler_item, parent, false))
            }
        }
    }

}
