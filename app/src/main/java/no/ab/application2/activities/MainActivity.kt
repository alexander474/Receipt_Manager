package no.ab.application2.activities

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import no.ab.application2.R
import no.ab.application2.fragments.ListFragment


/**
 * Got some inspiration regarding the toolbar/menu from https://www.youtube.com/watch?v=InF5tU_hecI (03.04.2019)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadElements()
        handleToolbar()
        setFirstTimeAppisLaunched()
        loadFragment()
    }


    private fun handleToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.menu_toolbar_search -> {
                return true
            }
            R.id.menu_settings_currency -> {
                chooseCurrencyDialog()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
            .setItems(list) { dialog, which ->
                val pref = getSharedPreferences(getString(R.string.setting_preference), Context.MODE_PRIVATE)
                pref.edit().putString(getString(R.string.settings_currency_id), list[which]).apply()
            }.create().show()
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

    private fun loadElements() {
        toolbar = findViewById(R.id.main_toolbar)
    }
}
