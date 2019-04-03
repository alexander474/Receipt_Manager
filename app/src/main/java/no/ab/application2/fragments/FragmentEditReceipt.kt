package no.ab.application2.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import no.ab.application2.CameraHandler
import no.ab.application2.IO.database.ReceiptEntity
import no.ab.application2.R
import no.ab.application2.Receipt
import no.ab.application2.Validation
import no.ab.application2.ViewModel.ReceiptViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FragmentEditReceipt : FragmentHandler() {

    private lateinit var receiptName: EditText
    private lateinit var receiptDescription: EditText
    private lateinit var receiptSum: EditText
    private lateinit var receiptSpinnerCurrency: Spinner
    private lateinit var takePicture: Button
    private lateinit var imageReceipt: ImageView
    private lateinit var saveReceipt: Button
    private lateinit var receiptViewModel: ReceiptViewModel
    private lateinit var receipt: Receipt
    private var currencySelected = ""
    private lateinit var cameraHandler: CameraHandler


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
            R.id.btn_edit_takePicture -> cameraHandler.takePicture().updateDisplayImage(imageReceipt)
            R.id.btn_edit_saveReciept -> updateReceipt()
        }
    }



    private fun validateInput(): Boolean{
        val v = Validation()
        val values = listOf(
            receiptName.text.toString(),
            receiptDescription.text.toString(),
            receiptSum.text.toString())
        return v.validate(values)
    }
    private fun updateReceipt() {
        if(validateInput()) {
            receiptViewModel.update(
                ReceiptEntity(
                    receipt.id,
                    receiptName.text.toString(),
                    receiptDescription.text.toString(),
                    receiptSum.text.toString().toDouble(),
                    currencySelected,
                    cameraHandler.currentPhotoPath,
                    receipt.dateCreated,
                    Date()
                )
            )
            popFragment(requireActivity(), 1)
        }else{
            // If some of the input entered is invalid
            AlertDialog.Builder(requireContext())
                .setTitle("Input is invalid")
                .setMessage("The input entered is invalid!")
                .setPositiveButton("I understand", DialogInterface.OnClickListener{ dialog, which ->
                    dialog.dismiss()
                }).create().show()
        }
    }

    private fun handleCurrencySpinner(currency: String){
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.currency_list)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        receiptSpinnerCurrency.adapter = adapter

        receiptSpinnerCurrency.setSelection(adapter.getPosition(currency))

        receiptSpinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //DO NOTHING
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currencySelected = parent!!.getItemAtPosition(position).toString()
            }
        }
    }



    private fun loadElements(view: View){
        receiptName = view.findViewById(R.id.input_edit_receipt_name)
        receiptDescription = view.findViewById(R.id.input_edit_receipt_decription)
        receiptSum = view.findViewById(R.id.input_edit_receipt_sum)
        receiptSpinnerCurrency = view.findViewById(R.id.input_edit_receipt_spinner_currency)
        imageReceipt = view.findViewById(R.id.image_edit_reciept)
        takePicture = view.findViewById(R.id.btn_edit_takePicture)
        saveReceipt = view.findViewById(R.id.btn_edit_saveReciept)
        receiptViewModel = ReceiptViewModel(activity!!.application)
        receipt = arguments!!.getSerializable(getString(R.string.receipt_bundle_id)) as Receipt
        cameraHandler = CameraHandler(requireActivity())

        receiptName.setText(receipt.name)
        receiptDescription.setText(receipt.description)
        receiptSum.setText(receipt.sum.toString())
        cameraHandler.currentPhotoPath = receipt.imagePath
        currencySelected = receipt.currency

        if(cameraHandler.currentPhotoPath.isNotEmpty()) cameraHandler.updateDisplayImage(imageReceipt)
        handleCurrencySpinner(receipt.currency)
    }

}