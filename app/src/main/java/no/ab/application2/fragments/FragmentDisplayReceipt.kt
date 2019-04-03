package no.ab.application2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import no.ab.application2.CameraHandler
import no.ab.application2.R
import no.ab.application2.Receipt

class FragmentDisplayReceipt : FragmentHandler(){

    private lateinit var name: TextView
    private lateinit var description: TextView
    private lateinit var sum: TextView
    private lateinit var currency: TextView
    private lateinit var image: ImageView
    private lateinit var created: TextView
    private lateinit var edited: TextView
    private lateinit var btn_edit: Button
    private lateinit var receipt: Receipt
    private lateinit var cameraHandler: CameraHandler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_display_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadElements(view)

        val btnListner = View.OnClickListener { handleButtonClick(it) }

        btn_edit.setOnClickListener(btnListner)

    }

    private fun handleButtonClick(it: View?) {
        when(it!!.id){
            R.id.display_btn_update -> updateReceipt()
        }
    }

    private fun updateReceipt() {
        val fragment = FragmentEditReceipt()
        val bundle = Bundle()
        bundle.putSerializable(context!!.getString(R.string.receipt_bundle_id), receipt)
        fragment.arguments = bundle
        pushFragmentWithStack(requireActivity(), R.id.fragment_container, fragment)
    }


    private fun loadElements(view: View) {
        name = view.findViewById(R.id.display_name)
        description = view.findViewById(R.id.display_description)
        sum = view.findViewById(R.id.display_sum)
        currency = view.findViewById(R.id.display_currency)
        image = view.findViewById(R.id.display_image)
        created = view.findViewById(R.id.display_created)
        edited = view.findViewById(R.id.display_edited)
        btn_edit = view.findViewById(R.id.display_btn_update)
        receipt = arguments!!.getSerializable(getString(R.string.receipt_bundle_id)) as Receipt
        cameraHandler = CameraHandler(requireActivity())

        name.text = receipt.name
        description.text = receipt.description
        sum.text = receipt.sum.toString()
        currency.text = receipt.currency
        created.text = receipt.dateCreated.toString()
        edited.text = receipt.dateLastModified.toString()
        cameraHandler.currentPhotoPath = receipt.imagePath
        if(cameraHandler.currentPhotoPath.isNotEmpty()) cameraHandler.updateDisplayImage(image)
    }


}