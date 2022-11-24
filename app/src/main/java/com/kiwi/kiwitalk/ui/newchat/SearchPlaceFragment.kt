package com.kiwi.kiwitalk.ui.newchat

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kiwi.domain.model.PlaceList
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentSearchPlaceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchPlaceFragment : Fragment() {

    private var _binding : FragmentSearchPlaceBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val searchPlaceViewModel: SearchPlaceViewModel by viewModels()

    private val permissionRequest = 99
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation : Location? = null

    private var markerState: Marker? = null
    private var baseMarker: BitmapDescriptor? = null
    private var selectMarker: BitmapDescriptor? = null

    private lateinit var locationCallback: LocationCallback
    private var permissions = arrayOf(
        ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
    )

    private val mapReadyCallback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.setMinZoomPreference(5.0F)
        mMap.setMaxZoomPreference(20.0F)
        mMap.clear()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity()) //gps 자동으로 받아오기

        setUpdateLocationListener()
        setMarkerClickListener()
        setMapClickListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPlaceBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.search_map) as SupportMapFragment?
        mapFragment?.getMapAsync(mapReadyCallback)

        if (!isPermitted()) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, permissionRequest)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchPlaceViewModel.isPlaceList.collect {
                    resultSearchPlace(it)
                }
            }
        }

        binding.btnKeywordSearch.setOnClickListener {
            searchLocation(currentLocation?:return@setOnClickListener,binding.etKeywordSearch.text.toString())
            binding.etKeywordSearch.text = null
        }
        with(binding){
            btnPlaceSave.setOnClickListener {
                val address = getAddress(requireContext(),
                    markerState?.position?.latitude?:return@setOnClickListener,
                    markerState?.position?.longitude?:return@setOnClickListener
                    )
                setDialog(address)

            }
        }
        baseMarker = bitmapDescriptorFromVector(requireContext(), R.drawable.ic_baseline_location_on_24)
        selectMarker = bitmapDescriptorFromVector(requireContext(), R.drawable.ic_baseline_location_on_click)

    }

    private fun isPermitted(): Boolean {
        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), perm)  != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun setUpdateLocationListener() {
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500000).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.withIndex().forEach {
                    setLastLocation(it.value)
                }
            }
        }
        //location 요청 함수 호출 (locationRequest, locationCallback)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private fun searchLocation(location: Location, keyword: String){
        mMap.clear()
        searchPlaceViewModel.getSearchPlace(location.longitude.toString(),location.latitude.toString(),keyword)
    }

    fun setLastLocation(location: Location) {
        currentLocation = location
        val myLocation = LatLng(location.latitude, location.longitude)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17.0F))
    }

    private fun resultSearchPlace(placeList: PlaceList){
        placeList.list.forEach { place ->
            val location = LatLng(place.lat.toDouble(),place.lng.toDouble())
            val markerOptions =
                MarkerOptions()
                    .position(location)
                    .title(place.placeName)
                    .icon(baseMarker)
            mMap.addMarker(markerOptions)
        }
    }

    private fun setMarkerClickListener() {
        mMap.setOnMarkerClickListener { marker ->
            binding.btnPlaceSave.visibility = View.VISIBLE
            markerState = if (markerState != null && markerState != marker) {
                clearMarkerClick(checkNotNull(markerState))
                marker.setIcon(selectMarker)
                marker
            } else {
                marker.setIcon(selectMarker)
                marker
            }
            // 마커 클릭 이벤트의 기본 동작 수행 (클릭시 카메라 이동, title 띄우기 등)
            false
        }
    }

    private fun setMapClickListener() {
        mMap.setOnMapClickListener {
            if (markerState != null) {
                binding.btnPlaceSave.visibility = View.GONE
                markerState?.setIcon(baseMarker)
                markerState = null
            }
        }
    }

    private fun setMapLongClickListener(){
        mMap.setOnMapLongClickListener {
            val markerOptions =
                MarkerOptions()
                    .position(it)
                    .icon(baseMarker)

            mMap.addMarker(markerOptions)
        }
    }
    private fun clearMarkerClick(marker: Marker) {
        marker.setIcon(baseMarker)
    }

    private fun getAddress(context: Context, lat: Double, lng: Double): String {
        var nowAddress: String = ADDRESS_ERROR
        val geocoder = Geocoder(context, Locale.KOREA)
        try {
            geocoder.changeLatLngToAddress(lat, lng) {
                if(it != null){
                    val currentLocationAddress: String = it.getAddressLine(0).toString()
                    nowAddress = currentLocationAddress
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return nowAddress
    }

    private fun setDialog(address: String){
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_new_chat, null)
        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .show()

        val textTitle = view.findViewById<TextView>(R.id.tv_current_address)
        val buttonConfirm =  view.findViewById<TextView>(R.id.btn_chat_place_save)
        val buttonClose =  view.findViewById<View>(R.id.btn_chat_place_cancel)
        textTitle.text = address

        dialog.window?.setGravity(Gravity.TOP)

        buttonClose.setOnClickListener {
            dialog.dismiss()
        }

        buttonConfirm.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.action_searchPlaceFragment_to_newChatFragment)
        }
    }
    // 벡터 이미지 변환
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}

