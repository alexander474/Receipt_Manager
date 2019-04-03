package no.ab.application2.activities

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import no.ab.application2.IO.database.ReceiptEntity
import no.ab.application2.R
import no.ab.application2.Receipt
import no.ab.application2.ReceiptSearchHandler
import no.ab.application2.ViewModel.ReceiptViewModel
import no.ab.application2.fragments.ListFragment
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


/**
 * Got some inspiration regarding the toolbar/menu from https://www.youtube.com/watch?v=InF5tU_hecI (03.04.2019)
 * Some inspiration regarding search from here https://code.luasoftware.com/tutorials/android/add-searchview-to-android-toolbar/ (03.04.2019)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var receiptViewModel: ReceiptViewModel
    private var receipts = ArrayList<Receipt>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        receiptViewModel = ReceiptViewModel(application)
        getAllReceipts()
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
        val searchItem = menu!!.findItem(R.id.menu_toolbar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            var timer = Timer()
            var list: List<Receipt>? = null
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                timer.cancel()
                val sleep = when(text!!.length){
                    1 -> 1000L
                    2,3 -> 700L
                    4,5 -> 500L
                    else -> 300L
                }
                timer = Timer()
                timer.schedule(sleep){
                    if(!text.isNullOrEmpty()){
                        list = ReceiptSearchHandler().search(receipts, text)

                    }
                }
                return true
            }
        })
        return super.onPrepareOptionsMenu(menu)
    }


    private fun getAllReceipts(){
        receiptViewModel.allReceiptsLive.observe(this, Observer { liveData ->
            liveData?.let { data ->
                receipts = mapToReceipt(data)
            }
        })
    }


    private fun mapToReceipt(data: List<ReceiptEntity>): ArrayList<Receipt>{
        val list = ArrayList<Receipt>()
        data.forEach { list.add(Receipt(it.id,it.name, it.description, it.sum, it.currency, it.imagePath, it.dateCreated, it.dateLastModified)) }
        return list
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.menu_toolbar_search -> {
                return true
            }
            R.id.menu_settings_currency -> {
                chooseCurrencyDialog(true)
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

    private fun chooseCurrencyDialog(restartRequired: Boolean = false){
        val list = resources.getStringArray(R.array.currency_list)
        AlertDialog.Builder(this)
            .setTitle("Please Choose Currency")
            .setItems(list) { dialog, which ->
                val pref = getSharedPreferences(getString(R.string.setting_preference), Context.MODE_PRIVATE)
                pref.edit().putString(getString(R.string.settings_currency_id), list[which]).apply()
            }.create().show()

        if(restartRequired){
            displaySnackbarThatRequiresRestart()
        }
    }

    /**
     * Alerts the user with a snackbar that restart to apply changes
     * The snackbar has a TTL at 5000ms (5seconds)
     */
    private fun displaySnackbarThatRequiresRestart(){
        Snackbar.make(fragment_container, "Needs to restart for changes to apply", Snackbar.LENGTH_LONG).apply {
            this.setAction("RESTART"){
                restart()
            }.setDuration(5000).show()
        }
    }

    /**
     * Restarts the activity
     */
    private fun restart(){
        Handler().post {
            val intent = intent
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_NO_ANIMATION
            )
            finish()
            startActivity(intent)
        }
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
