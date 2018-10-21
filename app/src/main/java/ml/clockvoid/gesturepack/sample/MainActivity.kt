package ml.clockvoid.gesturepack.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Button

class MainActivity : AppCompatActivity(), NavigationHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val vertical: Button? = findViewById(R.id.vertical_button)
        val horizontal: Button? = findViewById(R.id.horizontal_button)
        vertical?.setOnClickListener {
            navigateTo(VerticalFragment(), true)
        }
        horizontal?.setOnClickListener {
            navigateTo(HorizontalFragment(), true)
        }
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_frame, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}
