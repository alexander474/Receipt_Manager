package no.ab.application2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import no.ab.application2.fragments.ListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment()
    }

    /**
     * Initial load of fragment when the activity starts
     */
    private fun loadFragment(){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, ListFragment(), null)
            .commit()
    }
}
