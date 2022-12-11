package com.kiwi.kiwitalk.ui.newchat

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.ktx.markerClickEvents
import com.google.maps.android.ktx.myLocationButtonClickEvents
import com.kiwi.domain.model.PlaceInfoList
import com.kiwi.kiwitalk.NetworkStateManager
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentSearchPlaceBinding
import com.kiwi.kiwitalk.util.ChangeExpansion.changeLatLngToAddress
import com.kiwi.kiwitalk.util.Const.ADDRESS_ERROR
import com.kiwi.kiwitalk.util.Const.PERMISSION_CODE
import com.kiwi.kiwitalk.util.Util.changeVectorToBitmapDescriptor
import com.kiwi.kiwitalk.util.Util.generateVibrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class SearchPlaceFragment : Fragment() {

    private var _binding: FragmentSearchPlaceBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val searchPlaceViewModel: SearchPlaceViewModel by viewModels()

    private val permissionRequest = PERMISSION_CODE
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    private var markerState: Marker? = null
    private var baseMarker: BitmapDescriptor? = null
    private var selectMarker: BitmapDescriptor? = null
    private lateinit var networkConnectionState: NetworkStateManager

    private var permissions = arrayOf(
        ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
    )

    private val mapReadyCallback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        val styleOption = resources.assets.open("map_style.json").reader().readText()
        val mapStyleOptions = MapStyleOptions(styleOption)
        mMap.setMapStyle(mapStyleOptions)


        mMap.setMinZoomPreference(5.0F)
        mMap.setMaxZoomPreference(20.0F)
        mMap.clear()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity()) //gps 자동으로 받아오기

        updateLocationListener()
        setMarkerClickListener()
        setMapClickListener()
        setMapLongClickListener()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            mMap.myLocationButtonClickEvents().collectLatest {
                updateLocationListener()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPlaceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkConnectionState = NetworkStateManager(requireContext())
        networkConnectionState.register()

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.search_map) as SupportMapFragment?
        mapFragment?.getMapAsync(mapReadyCallback)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchPlaceViewModel.isPlaceList.collect {
                    resultSearchPlace(it)
                }
            }
        }

        with(binding) {
            btnKeywordSearch.setOnClickListener {
                hideKeyboard()
                searchLocation(
                    currentLocation ?: return@setOnClickListener,
                    etKeywordSearch.text.toString()
                )
                markerState = null
                mMap.clear()
                etKeywordSearch.text = null
            }
            btnPlaceSave.setOnClickListener {
                val address = getAddress(
                    requireContext(),
                    markerState?.position?.latitude ?: return@setOnClickListener,
                    markerState?.position?.longitude ?: return@setOnClickListener
                )
                if(address == ADDRESS_ERROR){
                    Snackbar.make(binding.root,"",Snackbar.LENGTH_SHORT).show()
                } else {
                    placeShowDialog(address)
                }

            }
        }
        baseMarker = changeVectorToBitmapDescriptor(requireContext(), R.drawable.ic_location_on)
        selectMarker = changeVectorToBitmapDescriptor(requireContext(), R.drawable.ic_location_on_click)
        initToolbar()
    }

    private fun initToolbar() {
        binding.searchPlaceMapToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun isPermitted(): Boolean {
        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), perm)
                != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun updateLocationListener() {
        checkPermission()
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
            task.addOnSuccessListener { location: Location? ->
                location ?: return@addOnSuccessListener
                currentLocation = location
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(location.latitude, location.longitude), 17f
                    )
                )
            }
        }
    }

    private fun searchLocation(location: Location, keyword: String) {
        searchPlaceViewModel.getSearchPlace(
            location.longitude.toString(),
            location.latitude.toString(),
            keyword
        )
    }

    private fun resultSearchPlace(placeList: PlaceInfoList) {
        if (::mMap.isInitialized.not()) return
        placeList.list?.forEach { place ->
            val location = LatLng(place.lat.toDouble(), place.lng.toDouble())

            val markerOptions =
                MarkerOptions()
                    .position(location)
                    .title(place.placeName)
                    .icon(baseMarker)

            mMap.addMarker(markerOptions)
        }
    }

    private fun setMarkerClickListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            mMap.markerClickEvents().collectLatest {
                markerState = if (markerState != null && markerState != it) {
                    checkNotNull(markerState).setIcon(baseMarker)
                    it.setIcon(selectMarker)
                    it
                } else {
                    it.setIcon(selectMarker)
                    it
                }
                changeAddButtonShowAndHide()
                it.showInfoWindow()
            }
        }
    }

    private fun setMapClickListener() {
        mMap.setOnMapClickListener {
            if (markerState != null) {
                markerState?.setIcon(baseMarker)
                markerState = null
                changeAddButtonShowAndHide()
            }
        }
    }

    private fun setMapLongClickListener() {
        mMap.setOnMapLongClickListener {
            val markerOptions =
                MarkerOptions()
                    .position(it)
                    .icon(baseMarker)
            mMap.clear()
            markerState = null
            changeAddButtonShowAndHide()
            mMap.addMarker(markerOptions)
            generateVibrator(requireContext())
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkPermission() {
        if (isPermitted()) {
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.isMyLocationEnabled = true
            return

        }
        ActivityCompat.requestPermissions(requireActivity(), permissions, permissionRequest)
    }

    private fun getAddress(context: Context, lat: Double, lng: Double): String {
        var nowAddress: String = ADDRESS_ERROR
        val geocoder = Geocoder(context, Locale.KOREA)

        geocoder.changeLatLngToAddress(lat, lng) {
            if (it != null) {
                val currentLocationAddress: String = it.getAddressLine(0).toString()
                nowAddress = currentLocationAddress
            }
        }
        return nowAddress
    }

    private fun placeShowDialog(address: String) {
        val msgBuilder= AlertDialog.Builder(context)
            .setTitle("주소")
            .setMessage(address)
            .setPositiveButton("취소") { _, _ ->
            }
            .setNegativeButton("저장") { _, _ ->
                findNavController().apply {
                    previousBackStackEntry?.savedStateHandle?.set(ADDRESS_KEY, address)
                    previousBackStackEntry?.savedStateHandle?.set(LATLNG_KEY, markerState?.position)
                    popBackStack()
                }
            }

        val msgDlg: AlertDialog = msgBuilder.create()
        msgDlg.show()
    }

    private fun changeAddButtonShowAndHide() {
        if (markerState != null) {
            val animation = TranslateAnimation(view?.width?.toFloat() ?: return, 0F, 0F, 0F)
            animation.duration = 200
            animation.fillAfter = true
            binding.btnPlaceSave.visibility = View.VISIBLE
            binding.btnPlaceSave.animation = animation
        } else {
            val animation = TranslateAnimation(0F, view?.width?.toFloat() ?: return, 0F, 0F)
            animation.duration = 200
            animation.fillAfter = true
            binding.btnPlaceSave.visibility = View.GONE
            binding.btnPlaceSave.animation = animation
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroy() {
        _binding = null
        networkConnectionState.unregister()
        super.onDestroy()
    }

    companion object {
        const val ADDRESS_KEY = "Address"
        const val LATLNG_KEY = "LatLng"
    }
}