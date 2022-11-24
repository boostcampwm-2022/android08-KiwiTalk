package com.kiwi.kiwitalk.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.myLocationButtonClickEvents
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.ActivitySearchChatBinding
import com.kiwi.kiwitalk.model.ClusterMarker
import com.kiwi.kiwitalk.model.ClusterMarker.Companion.toClusterMarker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchChatActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySearchChatBinding.inflate(layoutInflater) }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initMap()
        binding.tvSearchChatKeywords.setOnClickListener {
            viewModel.getMarkerList(listOf("축구", "영화"), 36.9, 127.0)
            getDeviceLocation()
        } //TODO: 검색버튼 만들고 제거
    }

    private fun initMap() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_searchChat_map) as? SupportMapFragment
                ?: return
        lifecycleScope.launchWhenCreated {
            map = mapFragment.awaitMap()
            getDeviceLocation()
            map.myLocationButtonClickEvents().collectLatest {
                getDeviceLocation()
            }
            setUpCluster()
        }
    }

    private suspend fun setUpCluster() {
        clusterManager = ClusterManager(this, map)
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.markerList.collect {
                Log.d("SearchChatActivity", "initMap: $it")
                clusterManager.addItem(it.toClusterMarker())
                clusterManager.cluster()
            }
        }

        map.setOnCameraIdleListener(clusterManager)
        clusterManager.setOnClusterItemClickListener { item ->
            Log.d("SearchChatActivity", "setOnClusterItemClickListener: $item")
            false
        }
        clusterManager.setOnClusterClickListener { cluster ->
            cluster.position
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
        val locationResult = fusedLocationClient.lastLocation
        locationResult.addOnCompleteListener(this) { task ->
            task.addOnSuccessListener { location: Location? ->
                location ?: return@addOnSuccessListener
                map.moveCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(location.latitude, location.longitude)
                    )
                )
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

    companion object {
        private val Any.TAG get() = this::class.simpleName
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}