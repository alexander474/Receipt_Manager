package no.ab.application2.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import no.ab.application2.IO.database.ReceiptEntity
import no.ab.application2.R
import no.ab.application2.Receipt
import no.ab.application2.ViewModel.ReceiptViewModel
import no.ab.application2.adapter.ReceiptAdapter

class ListFragment : FragmentHandler() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btn_newReceipt: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadElements(view)
        recyclerView = view.findViewById(R.id.receipt_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val receiptViewModel = ReceiptViewModel(activity!!.application)

        // Appends all the users in the UsersAdapter to the recyclerView
        receiptViewModel.allReceiptsLive.observe(this, Observer { liveData ->
            liveData?.let { data ->
                recyclerView.adapter = ReceiptAdapter(mapToReceipt(data), requireActivity())
            }
        })

        btn_newReceipt.setOnClickListener{pushFragmentWithStack(requireActivity(), R.id.fragment_container, FragmentNewReceipt())}

    }

    fun mapToReceipt(data: List<ReceiptEntity>): ArrayList<Receipt>{
        val list = ArrayList<Receipt>()
        data.forEach { list.add(Receipt(it.id,it.name, it.description, it.sum, it.currency, it.imagePath)) }
        return list
    }

    fun loadElements(view: View){
        btn_newReceipt = view.findViewById(R.id.btn_newReceipt)
    }

}