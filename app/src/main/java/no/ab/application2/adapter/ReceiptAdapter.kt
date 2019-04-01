package no.ab.application2.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import no.ab.application2.R
import no.ab.application2.Receipt


class ReceiptAdapter(val receipts: ArrayList<Receipt>) : RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = receipts[position].name
        holder.description.text = receipts[position].description
        holder.sum.text = receipts[position].sum.toString()
        holder.bind(receipts[position])

        holder.item.setOnClickListener{ it ->
                val expanded: Boolean = receipts[position].isExpanded
                receipts[position].isExpanded = !expanded
                notifyItemChanged(position)
        }

    }

    override fun getItemCount() = receipts.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(receipt: Receipt) {
            val subItem: LinearLayout = itemView.findViewById(R.id.list_row_item_sub)
            val expanded: Boolean = receipt.isExpanded
            subItem.visibility = if(expanded) View.VISIBLE else View.GONE
        }

        val name: TextView = itemView.findViewById(R.id.list_row_name)
        val description: TextView = itemView.findViewById(R.id.list_row_description)
        val sum: TextView = itemView.findViewById(R.id.list_row_sum)
        val item: LinearLayout = itemView.findViewById(R.id.list_row_item)


    }
}