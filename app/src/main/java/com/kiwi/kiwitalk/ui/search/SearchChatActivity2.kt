package com.kiwi.kiwitalk.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kiwi.domain.model.Marker
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.ActivitySearchChat2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchChatActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivitySearchChat2Binding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: SearchChatViewModel by viewModels()
    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_chat_2)

        initBottomSheetCallBack()

        /* 마커 클릭시 이처럼 STATE 변경 코드 넣어줘야함 */
        binding.fabCreateChat.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.getPlaceInfo(Marker("messaging:-149653492", 1.0, 1.0, listOf()))
        }

        /* 지도 누르면 바텀시트 사라져야함 */
        binding.rootLayout.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)
    }

    private fun initBottomSheetCallBack() {
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d(TAG, newState.toString())
                when (newState) {
                    // 1일때: 확장 모드로 가거나, 접히는 중임. 전체 채팅방 정보 없으면 로딩하고 있으면 유지.
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        binding.layoutMarkerInfoPreview.rootLayout.visibility = View.GONE
                    }
                    // 4 되었을 때: 시트 접힘. 대표 채팅방 정보, 채팅방 수만 표시
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.layoutMarkerInfoPreview.rootLayout.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // slideOffset : top = 1, bottom = 0이다. 위치에 따른 동작을 설정할 수 있다.
            }
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.infoBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    companion object {
        private const val TAG = "k001"
    }
}