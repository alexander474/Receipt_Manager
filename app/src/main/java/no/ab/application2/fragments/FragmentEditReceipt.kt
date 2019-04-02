package no.ab.application2.fragments

import android.Manifest
import android.app.AlertDialog
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
    val REQUEST_TAKE_PHOTO = 1
    private var currentPhotoPath: String = ""


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
            R.id.btn_edit_takePicture -> checkCameraPermissions()
            R.id.btn_edit_saveReciept -> updateReceipt()
        }
    }

    fun makeToast(message: String){
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    fun checkCameraPermissions(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            takeNewPicture()
        } else requestCameraPermission()
    }

    private fun requestCameraPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)){
            AlertDialog.Builder(requireContext())
                .setTitle("Permission Needed")
                .setMessage("This permission is needed to store image of the receipt")
                .setPositiveButton("OK", DialogInterface.OnClickListener{ dialog, which ->
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_TAKE_PHOTO)
                })
                .setNegativeButton("cancel", DialogInterface.OnClickListener{ dialog, which ->
                    dialog.dismiss()
                }).create().show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_TAKE_PHOTO)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_TAKE_PHOTO){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takeNewPicture()
            }else{
                makeToast("Cannot take picture without permission to access camera")
            }
        }
    }

    private fun takeNewPicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        activity!!.application,
                        "no.ab.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
        updateDisplayImage()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        deleteImageOnRetake()
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun deleteImageOnRetake(){
        if(currentPhotoPath.isNotEmpty()) {
            val file = File(currentPhotoPath)
            if(file.delete()) makeToast("Image deleted")
        }
    }

    private fun updateDisplayImage(){
        val bitMap = BitmapFactory.decodeFile(File(currentPhotoPath).absolutePath)
        imageReceipt.setImageBitmap(bitMap)
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
                    currentPhotoPath
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
        receipt = arguments!!.getSerializable("receipt") as Receipt

        receiptName.setText(receipt.name)
        receiptDescription.setText(receipt.description)
        receiptSum.setText(receipt.sum.toString())
        currentPhotoPath = receipt.imagePath
        currencySelected = receipt.currency

        if(currentPhotoPath.isNotEmpty()) updateDisplayImage()
        handleCurrencySpinner()
    }

}