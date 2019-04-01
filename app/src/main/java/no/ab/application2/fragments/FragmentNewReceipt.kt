package no.ab.application2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import no.ab.application2.IO.ReceiptEntity
import no.ab.application2.R
import no.ab.application2.ViewModel.ReceiptViewModel

class FragmentNewReceipt : FragmentHandler() {

    private lateinit var receiptName: EditText
    private lateinit var receiptDescription: EditText
    private lateinit var receiptSum: EditText
    private lateinit var createReceipt: Button
    private lateinit var receiptViewModel: ReceiptViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadElements(view)

        createReceipt.setOnClickListener{
            receiptViewModel.insert(ReceiptEntity(receiptName.text.toString(), receiptDescription.text.toString(), receiptSum.text.toString().toDouble()))
            popFragment(requireActivity(), 1)
        }



    }

    private fun loadElements(view: View){
        receiptName = view.findViewById(R.id.input_receipt_name)
        receiptDescription = view.findViewById(R.id.input_receipt_decription)
        receiptSum = view.findViewById(R.id.input_receipt_sum)
        createReceipt = view.findViewById(R.id.btn_createReciept)
        receiptViewModel = ReceiptViewModel(activity!!.application)
    }

}