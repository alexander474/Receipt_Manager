package no.ab.application2.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import no.ab.application2.R
import no.ab.application2.Receipt


class ReceiptAdapter(private val receipts: ArrayList<Receipt>) : RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = receipts[position].name
        holder.description.text = receipts[position].description
        holder.sum.text = receipts[position].sum.toString()
        holder.bind(receipts[position])

        holder.item_row.setOnClickListener{ it ->
                val expanded: Boolean = receipts[position].isExpanded
                receipts[position].isExpanded = !expanded
                notifyItemChanged(position)
        }

        holder.btn_edit.setOnClickListener{}

    }

    override fun getItemCount() = receipts.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(receipt: Receipt) {
            val item_row_subItem: LinearLayout = itemView.findViewById(R.id.list_row_item_sub)
            val expanded: Boolean = receipt.isExpanded
            item_row_subItem.visibility = if(expanded) View.VISIBLE else View.GONE
        }

        val name: TextView = itemView.findViewById(R.id.list_row_name)
        val description: TextView = itemView.findViewById(R.id.list_row_description)
        val sum: TextView = itemView.findViewById(R.id.list_row_sum)
        val btn_edit: Button = itemView.findViewById(R.id.list_row_btn_update)
        val item_row: LinearLayout = itemView.findViewById(R.id.list_row_item)

    }
}