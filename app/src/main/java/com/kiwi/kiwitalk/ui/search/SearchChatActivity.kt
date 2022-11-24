package com.kiwi.kiwitalk.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.myLocationButtonClickEvents
import com.kiwi.domain.model.Marker
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.ActivitySearchChatBinding
import com.kiwi.kiwitalk.model.ClusterMarker
import com.kiwi.kiwitalk.model.ClusterMarker.Companion.toClusterMarker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchChatBinding
    private val viewModel: SearchChatViewModel by viewModels()
    private lateinit var map: GoogleMap
    private lateinit var clusterManager: ClusterManager<ClusterMarker>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onStart() {
        super.onStart()
        if (this::map.isInitialized) {
            checkPermission()
        }
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_chat)
        binding.vm = viewModel

        initMap()
        binding.tvSearchChatKeywords.setOnClickListener {
            viewModel.getMarkerList(listOf("축구", "영화"), 36.9, 127.0)
        } //TODO: FloatingButton 만들고 제거

        initBottomSheetCallBack()

        /* TODO 마커 클릭으로 바꿔야함 */
        binding.fabCreateChat.setOnClickListener {
            // newChatActivity로 바꾸는 코드로 대체해야함
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.getPlaceInfo(Marker("messaging:-149653492", 1.0, 1.0, listOf()))
        }

        /* TODO 바깥 누르면 바텀시트 내려가게하기 */
        binding.fragmentSearchChatMap.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun initMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_searchChat_map) as? SupportMapFragment
                ?: return
        lifecycleScope.launchWhenCreated {
            map = mapFragment.awaitMap()
            getDeviceLocation()
            setUpCluster()
            map.myLocationButtonClickEvents().collectLatest {
                getDeviceLocation()
            }
        }
    }

    private fun setUpCluster() {
        clusterManager = ClusterManager(this, map)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.markerList.collect {
                    Log.d("SearchChatActivity", "initMap: $it")
                    clusterManager.addItem(it.toClusterMarker())
                    clusterManager.cluster()
                }
            }
        }

        map.setOnCameraIdleListener(clusterManager)
        clusterManager.setOnClusterItemClickListener { item ->
            Log.d("SearchChatActivity", "setOnClusterItemClickListener: $item")
            false
        }
        clusterManager.setOnClusterClickListener { cluster ->
            Log.d("SearchChatActivity", "setOnClusterClickListener: $cluster")
            cluster.items.forEach {
                Log.d("SearchChatActivity", "forEach: $it")
            }
            false
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        checkPermission()

        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            task.addOnSuccessListener { location: Location? ->
                Log.d(TAG, "getDeviceLocation location: $location")
                location ?: return@addOnSuccessListener
                map.moveCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(location.latitude, location.longitude)
                    )
                )
            }.addOnFailureListener {

            }
        }
    }

    private fun isGranted(): Boolean {
        Log.d(TAG, "isGranted start")
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(this::class.simpleName, "onRequestPermissionsResult start")
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (isGranted()) {
            checkPermission()
        } else {
            Toast.makeText(this, "권한이 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkPermission() {
        Log.d(TAG, "enableMyLocation start")
        if (isGranted()) {
            map.uiSettings.isMyLocationButtonEnabled = true
            map.isMyLocationEnabled = true
            return
        }

        ActivityCompat.requestPermissions(
            this, permissions, LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun initBottomSheetCallBack() {
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d(TAG, newState.toString())
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        //binding.layoutMarkerInfoPreview.rootLayout.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        //binding.layoutMarkerInfoPreview.rootLayout.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }


    override fun onDestroy() {
        super.onDestroy()
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)
    }

    companion object {
        private val Any.TAG get() = this::class.simpleName
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val TAG = "k001"
    }
}