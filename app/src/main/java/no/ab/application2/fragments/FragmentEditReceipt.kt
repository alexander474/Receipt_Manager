package no.ab.application2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import no.ab.application2.IO.database.ReceiptEntity
import no.ab.application2.R
import no.ab.application2.Receipt
import no.ab.application2.ViewModel.ReceiptViewModel

class FragmentEditReceipt : FragmentHandler() {

    private lateinit var receiptName: EditText
    private lateinit var receiptDescription: EditText
    private lateinit var receiptSum: EditText
    private lateinit var takePicture: Button
    private lateinit var imageReceipt: ImageView
    private lateinit var saveReceipt: Button
    private lateinit var receiptViewModel: ReceiptViewModel
    private lateinit var receipt: Receipt


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadElements(view)

        val btnListner = View.OnClickListener { handleButtonClick(it) }

        takePicture.setOnClickListener(btnListner)
        saveReceipt.setOnClickListener(btnListner)



    }

    private fun handleButtonClick(view: View){
        when(view.id){
            R.id.btn_edit_saveReciept -> createNewReceipt()
        }
    }


    private fun createNewReceipt() {
        receiptViewModel.update(
            ReceiptEntity(
                receipt.id,
                receiptName.text.toString(),
                receiptDescription.text.toString(),
                receiptSum.text.toString().toDouble()
            )
        )
        popFragment(requireActivity(), 1)
    }


    private fun loadElements(view: View){
        receiptName = view.findViewById(R.id.input_edit_receipt_name)
        receiptDescription = view.findViewById(R.id.input_edit_receipt_decription)
        receiptSum = view.findViewById(R.id.input_edit_receipt_sum)
        imageReceipt = view.findViewById(R.id.image_edit_reciept)
        takePicture = view.findViewById(R.id.btn_edit_takePicture)
        saveReceipt = view.findViewById(R.id.btn_edit_saveReciept)
        receiptViewModel = ReceiptViewModel(activity!!.application)
        receipt = arguments!!.getSerializable("receipt") as Receipt

        receiptName.setText(receipt.name)
        receiptDescription.setText(receipt.description)
        receiptSum.setText(receipt.sum.toString())


    }

}