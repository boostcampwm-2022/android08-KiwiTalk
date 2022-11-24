package com.kiwi.kiwitalk.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kiwi.domain.model.Marker
import com.kiwi.domain.model.keyword.Keyword
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.ActivitySearchChatBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.KeywordAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchChatActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivitySearchChatBinding
    private val viewModel: SearchChatViewModel by viewModels()
    private lateinit var map: GoogleMap

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_chat)
        binding.lifecycleOwner = this
        binding.vm = viewModel

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

        val adapter = KeywordAdapter(mutableListOf<Keyword>())
        binding.layoutMarkerInfoPreview.rvChatKeywords.adapter = adapter
        viewModel.placeChatInfo.observe(this){
            adapter.updateList(it.getPopularChat().keywords.map { Keyword(it, 0) }.toMutableList())
        }

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

    private fun initBottomSheetCallBack() {
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        binding.layoutMarkerInfoPreview.rootLayout.visibility = View.GONE
                        binding.tvDetail.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.layoutMarkerInfoPreview.rootLayout.visibility = View.VISIBLE
                        binding.tvDetail.visibility = View.GONE
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
}