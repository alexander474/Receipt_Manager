package no.ab.application2.adapter

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import no.ab.application2.R
import no.ab.application2.Receipt
import no.ab.application2.fragments.FragmentDisplayReceipt
import no.ab.application2.fragments.FragmentEditReceipt
import org.json.JSONObject


/**
 * https://www.youtube.com/watch?v=y2xtLqP8dSQ (28.03.2019)
 */
class ReceiptAdapter(
    private val receipts: ArrayList<Receipt>,
    private val activity: FragmentActivity
) : RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentReceipt = receipts[position]
        holder.name.text = currentReceipt.name
        holder.description.text = currentReceipt.description
        setCurrencyData(holder, currentReceipt.sum, currentReceipt.currency)
        askForData(holder,currentReceipt)

        holder.bind(currentReceipt)

        holder.item_row.setOnClickListener{
                val expanded: Boolean = currentReceipt.isExpanded
                currentReceipt.isExpanded = !expanded
                notifyItemChanged(position)
        }

        holder.btn_edit.setOnClickListener{
            val fragment = FragmentEditReceipt()
            val bundle = Bundle()
            bundle.putSerializable(activity.getString(R.string.receipt_bundle_id), currentReceipt)
            fragment.arguments = bundle
            activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .addToBackStack(null)
                .commit()
        }
        holder.btn_open.setOnClickListener{
            val fragment = FragmentDisplayReceipt()
            val bundle = Bundle()
            bundle.putSerializable(activity.getString(R.string.receipt_bundle_id), currentReceipt)
            fragment.arguments = bundle
            activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .addToBackStack(null)
                .commit()
        }

    }

    private fun askForData(holder: ViewHolder, currentReceipt: Receipt){
        val currencyFromPreferences = getSharedPrefCurrency()
        if(currencyFromPreferences != null && checkInternetPermission()){
            checkCurrentExchangeRates(holder, currentReceipt, currencyFromPreferences)
        }
    }

    private fun setCurrencyData(holder: ViewHolder, sum: Double, currency: String){
        holder.sum.text = sum.toString()
        holder.currency.text = currency
    }


    private fun checkInternetPermission(): Boolean{
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork.isConnected
    }


    private fun checkCurrentExchangeRates(holder: ViewHolder, receipt: Receipt, currency: String){
        val queue = Volley.newRequestQueue(activity)
        val JASON_OBJECT_REQUEST = JsonObjectRequest(
            Request.Method.GET,
            currencyQueryQreation(receipt.currency, currency),
            null,
            Response.Listener{ response ->
                setCurrencyData(holder,parseRequest(receipt, currency, response), currency)
            },
            Response.ErrorListener {
                //ERROR
            }
        )
        queue.add(JASON_OBJECT_REQUEST)
        queue.start()
    }

    /**
     * @return calculated exchange rates between currency that user wants and the currency the receipt is stored with
     */
    private fun parseRequest(receipt: Receipt, currency: String, result: JSONObject): Double {
        val rates = result.getJSONObject("rates")
        val newCurrency:Double = rates.getDouble(currency)
        return  receipt.sum * newCurrency
    }

    /**
     * @return the full uri for the api request
     */
    private fun currencyQueryQreation(from: String, to: String):String{
        return activity.getString(R.string.currency_api_path)+"base=${from}&symbols=${to}"
    }

    private fun getSharedPrefCurrency(): String?{
        val pref = activity.getSharedPreferences(activity.getString(R.string.setting_preference), Context.MODE_PRIVATE)
        return pref.getString(activity.getString(R.string.settings_currency_id), activity.getString(R.string.settings_currency_defaultvalue))
    }


    override fun getItemCount() = receipts.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.list_row_name)
        val description: TextView = itemView.findViewById(R.id.list_row_description)
        val sum: TextView = itemView.findViewById(R.id.list_row_sum)
        val currency: TextView = itemView.findViewById(R.id.list_row_currency)
        val btn_edit: Button = itemView.findViewById(R.id.list_row_btn_update)
        val btn_open: Button = itemView.findViewById(R.id.list_row_btn_open)
        val item_row: CardView = itemView.findViewById(R.id.list_row_card_item)
        val item_row_subItem: LinearLayout = itemView.findViewById(R.id.list_row_item_sub)

        fun bind(receipt: Receipt) {
            val expanded: Boolean = receipt.isExpanded
            item_row_subItem.visibility = if(expanded) View.VISIBLE else View.GONE
        }

    }
}