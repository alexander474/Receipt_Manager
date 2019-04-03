package no.ab.application2.activities

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import no.ab.application2.R
import no.ab.application2.fragments.ListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFirstTimeAppisLaunched()
        loadFragment()
    }


    private fun setFirstTimeAppisLaunched(){
        val pref = getSharedPreferences(getString(R.string.setting_preference), Context.MODE_PRIVATE)
        val firstTime = pref.getBoolean(getString(R.string.settings_firstTime_id), true)
        if(firstTime){
            pref.edit().putBoolean(getString(R.string.settings_firstTime_id), false).apply()
            chooseCurrencyDialog()
        }
        val currencyIsChoosen = pref.getString(getString(R.string.settings_currency_id), getString(R.string.settings_currency_defaultvalue))
        if(currencyIsChoosen == getString(R.string.settings_currency_defaultvalue)){
            chooseCurrencyDialog()
        }
    }

    private fun chooseCurrencyDialog(){
        val list = resources.getStringArray(R.array.currency_list)
        AlertDialog.Builder(this)
            .setTitle("Please Choose Currency")
            .setItems(list, DialogInterface.OnClickListener { dialog, which ->
                val pref = getSharedPreferences(getString(R.string.setting_preference), Context.MODE_PRIVATE)
                pref.edit().putString(getString(R.string.settings_currency_id), list[which]).apply()
            }).create().show()
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
