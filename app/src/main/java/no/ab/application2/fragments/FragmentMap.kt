package no.ab.application2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import no.ab.application2.R

class FragmentMap : FragmentHandler(), OnMapReadyCallback{


    private lateinit var map: GoogleMap



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }



    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.uiSettings.isZoomControlsEnabled = true


        val fjerdingen = LatLng(59.9160606, 10.7599732)
        val vulkan = LatLng(59.9233093261719, 10.7521686553955)
        val kvaderaturen = LatLng(59.9112129211426, 10.7448539733887)
        var marker_fjerdingen: Marker
        var marker_vulkan: Marker
        var marker_kvaderaturen: Marker

        marker_fjerdingen = map.addMarker(
            MarkerOptions()
                .position(fjerdingen)
                .title("Fjerdingen")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )

        marker_vulkan = map.addMarker(
            MarkerOptions()
                .position(vulkan)
                .title("Vulkan")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )

        marker_kvaderaturen = map.addMarker(
            MarkerOptions()
                .position(kvaderaturen)
                .title("Kvaderaturen")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )



        map.animateCamera(CameraUpdateFactory.newLatLngZoom(fjerdingen, 12f))
    }


}