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
import com.google.android.gms.maps.model.MarkerOptions
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.ActivitySearchChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchChatActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by lazy { ActivitySearchChatBinding.inflate(layoutInflater) }
    private val viewModel: SearchChatViewModel by viewModels()
    private lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initMap()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.markerList.collect {
                    Log.d("SearchChatActivity", "onCreate: $it")
                }
            }
        }
        binding.tvSearchChatKeywords.setOnClickListener {
            viewModel.getMarkerList(emptyList(), 0.0, 0.0)
        } //TODO: FloatingButton 만들고 제거
    }

    private fun initMap() {
        Log.d("SearchChatActivity", "initMap")
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_searchChat_map) as? SupportMapFragment
        Log.d("SearchChatActivity", "mapFragment : $mapFragment")
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(_map: GoogleMap) {
        Log.d(this::class.simpleName, "onMapReady: ")
        map = _map
        val seoul = LatLng(37.5666805, 126.9784147)
        map.addMarker(
            MarkerOptions().position(seoul).title("서울")
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10f))
    }
}