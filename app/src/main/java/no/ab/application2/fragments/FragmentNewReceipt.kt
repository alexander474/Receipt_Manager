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
import kotlinx.android.synthetic.main.fragment_new_receipt.*
import no.ab.application2.CameraHandler
import no.ab.application2.IO.database.ReceiptEntity
import no.ab.application2.R
import no.ab.application2.Validation
import no.ab.application2.ViewModel.ReceiptViewModel
import java.io.File
import java.io.IOException
import java.nio.file.Files.createFile
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


/**
 * Some of the code regarding taking images is inspired from this page
 * https://developer.android.com/training/camera/photobasics (02.04.2019)
 */
class FragmentNewReceipt : FragmentHandler() {

    private lateinit var receiptName: EditText
    private lateinit var receiptDescription: EditText
    private lateinit var receiptSum: EditText
    private lateinit var receiptSpinnerCurrency: Spinner
    private lateinit var takePicture: Button
    private lateinit var imageReceipt: ImageView
    private lateinit var createReceipt: Button
    private lateinit var receiptViewModel: ReceiptViewModel
    private var currencySelected = ""
    private lateinit var cameraHandler: CameraHandler


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadElements(view)

        val btnListner = View.OnClickListener { handleButtonClick(it) }

        takePicture.setOnClickListener(btnListner)
        createReceipt.setOnClickListener(btnListner)



    }

    private fun handleButtonClick(view: View){
        when(view.id){
            R.id.btn_takePicture -> cameraHandler.takePicture().updateDisplayImage(imageReceipt)
            R.id.btn_createReciept -> createNewReceipt()
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

    private fun createNewReceipt() {
        if(validateInput()) {
            receiptViewModel.insert(
                ReceiptEntity(
                    receiptName.text.toString(),
                    receiptDescription.text.toString(),
                    receiptSum.text.toString().toDouble(),
                    currencySelected,
                    cameraHandler.currentPhotoPath,
                    Date(),
                    Date()
                )
            )
            popFragment(requireActivity(), 1)
        }else{
            AlertDialog.Builder(requireContext())
                .setTitle("Input is invalid")
                .setMessage("The input entered is invalid!")
                .setPositiveButton("I understand", DialogInterface.OnClickListener{ dialog, which ->
                    dialog.dismiss()
                }).create().show()
        }
    }

    private fun handleCurrencySpinner(){
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.currency_list)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        receiptSpinnerCurrency.adapter = adapter

        val currentCurrency = getSharedPrefCurrency()
        if(currentCurrency != getString(R.string.settings_currency_defaultvalue)){
            receiptSpinnerCurrency.setSelection(adapter.getPosition(currentCurrency))
        }

        receiptSpinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //DO NOTHING
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currencySelected = parent!!.getItemAtPosition(position).toString()
            }
        }
    }

    private fun getSharedPrefCurrency(): String?{
        val pref = requireActivity().getSharedPreferences(getString(R.string.setting_preference), Context.MODE_PRIVATE)
        return pref.getString(getString(R.string.settings_currency_id), getString(R.string.settings_currency_defaultvalue))
    }



    private fun loadElements(view: View){
        receiptName = view.findViewById(R.id.input_receipt_name)
        receiptDescription = view.findViewById(R.id.input_receipt_decription)
        receiptSum = view.findViewById(R.id.input_receipt_sum)
        receiptSpinnerCurrency = view.findViewById(R.id.input_receipt_spinner_currency)
        imageReceipt = view.findViewById(R.id.image_reciept)
        takePicture = view.findViewById(R.id.btn_takePicture)
        createReceipt = view.findViewById(R.id.btn_createReciept)
        receiptViewModel = ReceiptViewModel(activity!!.application)
        cameraHandler = CameraHandler(requireActivity())
        handleCurrencySpinner()
    }


}