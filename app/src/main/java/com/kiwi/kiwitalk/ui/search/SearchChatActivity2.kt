package com.kiwi.kiwitalk.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kiwi.kiwitalk.databinding.ActivitySearchChat2Binding

class SearchChatActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivitySearchChat2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchChat2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d(TAG, newState.toString())

                // 4 -> 1일때: 시트 확장중. 모든 채팅방 정보 로딩

                // 4 되었을 때: 시트 접힘. 대표 채팅방 정보, 채팅방 수만 표시
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // slideOffset : top = 1, bottom = 0이다. 위치에 따른 동작을 설정할 수 있다.
                Log.d(TAG, "onSlide. offset : $slideOffset")
            }
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        /* 마커 클릭시 이처럼 STATE 변경 코드 넣어줘야함 */
        binding.fabCreateChat.setOnClickListener{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val TAG = "k001"
    }
}