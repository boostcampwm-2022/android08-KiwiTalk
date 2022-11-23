package com.kiwi.kiwitalk.ui.search

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.ActivitySearchChatBinding
import com.kiwi.kiwitalk.model.ClusterMarker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchChatActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by lazy { ActivitySearchChatBinding.inflate(layoutInflater) }
    private val viewModel: SearchChatViewModel by viewModels()
    private lateinit var map: GoogleMap
    private lateinit var clusterManager: ClusterManager<ClusterMarker>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initMap()
        binding.tvSearchChatKeywords.setOnClickListener {
            viewModel.getMarkerList(emptyList(), 0.0, 0.0)
        } //TODO: FloatingButton 만들고 제거
    }

    private fun initMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_searchChat_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.markerList.collect {
                    Log.d("SearchChatActivity", "initMap: $it")
                    clusterManager.addItem(ClusterMarker(it.x, it.y, it.cid, it.toString()))
                    clusterManager.cluster()
                }
            }
        }
    }

    override fun onMapReady(_map: GoogleMap) {
        Log.d(this::class.simpleName, "onMapReady")
        map = _map
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5666805, 126.9784147), 10f))
        setUpCluster()
    }

    private fun setUpCluster() {
        clusterManager = ClusterManager(this, map)
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
}

