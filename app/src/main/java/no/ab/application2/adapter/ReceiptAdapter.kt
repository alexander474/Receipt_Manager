package no.ab.application2.adapter

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
import no.ab.application2.R
import no.ab.application2.Receipt
import no.ab.application2.fragments.FragmentEditReceipt


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
        holder.sum.text = currentReceipt.sum.toString()
        holder.bind(currentReceipt)

        holder.item_row.setOnClickListener{
                val expanded: Boolean = currentReceipt.isExpanded
                currentReceipt.isExpanded = !expanded
                notifyItemChanged(position)
        }

        holder.btn_edit.setOnClickListener{
            val fragment = FragmentEditReceipt()
            val bundle = Bundle()
            bundle.putSerializable("receipt", currentReceipt)
            fragment.arguments = bundle
            activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .addToBackStack(null)
                .commit()
        }


    }


    override fun getItemCount() = receipts.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.list_row_name)
        val description: TextView = itemView.findViewById(R.id.list_row_description)
        val sum: TextView = itemView.findViewById(R.id.list_row_sum)
        val btn_edit: Button = itemView.findViewById(R.id.list_row_btn_update)
        val item_row: CardView = itemView.findViewById(R.id.list_row_card_item)
        val item_row_subItem: LinearLayout = itemView.findViewById(R.id.list_row_item_sub)

        fun bind(receipt: Receipt) {
            val expanded: Boolean = receipt.isExpanded
            item_row_subItem.visibility = if(expanded) View.VISIBLE else View.GONE
        }

    }
}