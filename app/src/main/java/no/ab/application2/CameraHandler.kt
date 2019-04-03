package no.ab.application2

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraHandler(val activity: Activity) : ActivityCompat.OnRequestPermissionsResultCallback{

    private val REQUEST_TAKE_PHOTO = 9999
    var  currentPhotoPath: String = ""

    fun takePicture(): CameraHandler{
        checkCameraPermissions()
        return this
    }

    fun updateDisplayImage(imageView: ImageView?): CameraHandler{
        if(imageView != null) {
            val bitMap = BitmapFactory.decodeFile(File(currentPhotoPath).absolutePath)
            imageView.setImageBitmap(bitMap)
        }
        return this
    }

    private fun checkCameraPermissions(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            takeNewPicture()
        } else requestCameraPermission()
    }

    private fun requestCameraPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
            AlertDialog.Builder(activity)
                .setTitle("Permission Needed")
                .setMessage("This permission is needed to store image of the receipt")
                .setPositiveButton("OK", DialogInterface.OnClickListener{ dialog, which ->
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_TAKE_PHOTO)
                })
                .setNegativeButton("cancel", DialogInterface.OnClickListener{ dialog, which ->
                    dialog.dismiss()
                }).create().show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_TAKE_PHOTO)
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
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
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
                    activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)

                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        deleteImageOnRetake()
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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


    fun makeToast(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}