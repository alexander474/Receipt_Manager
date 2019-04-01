package no.ab.application2.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_new_receipt.*
import no.ab.application2.IO.database.ReceiptEntity
import no.ab.application2.R
import no.ab.application2.ViewModel.ReceiptViewModel
import java.io.File
import java.io.IOException
import java.nio.file.Files.createFile
import java.text.SimpleDateFormat
import java.util.*

class FragmentNewReceipt : FragmentHandler() {

    private lateinit var receiptName: EditText
    private lateinit var receiptDescription: EditText
    private lateinit var receiptSum: EditText
    private lateinit var takePicture: Button
    private lateinit var imageReceipt: ImageView
    private lateinit var createReceipt: Button
    private lateinit var receiptViewModel: ReceiptViewModel
    val REQUEST_TAKE_PHOTO = 1
    private lateinit var currentPhotoPath: String


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
            R.id.btn_takePicture -> if(checkPermissions())takeNewPicture()
            R.id.btn_createReciept -> createNewReceipt()
        }
    }

    fun checkPermissions():Boolean{
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 1);
        }else return true
        return false
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
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
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

    private fun createNewReceipt() {
        receiptViewModel.insert(
            ReceiptEntity(
                receiptName.text.toString(),
                receiptDescription.text.toString(),
                receiptSum.text.toString().toDouble()
            )
        )
        popFragment(requireActivity(), 1)
    }


    private fun loadElements(view: View){
        receiptName = view.findViewById(R.id.input_receipt_name)
        receiptDescription = view.findViewById(R.id.input_receipt_decription)
        receiptSum = view.findViewById(R.id.input_receipt_sum)
        imageReceipt = view.findViewById(R.id.image_reciept)
        takePicture = view.findViewById(R.id.btn_takePicture)
        createReceipt = view.findViewById(R.id.btn_createReciept)
        receiptViewModel = ReceiptViewModel(activity!!.application)
    }

}