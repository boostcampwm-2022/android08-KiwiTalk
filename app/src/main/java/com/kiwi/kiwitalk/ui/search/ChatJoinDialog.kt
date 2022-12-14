package com.kiwi.kiwitalk.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.kiwi.domain.model.ChatInfo
import com.kiwi.domain.model.Keyword
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.DialogJoinChatBinding
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter

class ChatJoinDialog(
    private val chatDialogAction: ChatDialogAction,
    private val chatInfo: ChatInfo,
) : DialogFragment() {

    private var _binding: DialogJoinChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_join_chat, container, false)
        binding.chatInfo = chatInfo
        binding.adapter = SelectedKeywordAdapter().apply {
            submitList(chatInfo.keywords.map {
                Keyword(it, 0)
            }.toMutableList())
        }
        binding.btnDialogClose.setOnClickListener {
            chatDialogAction.onClickCancleButton()
            dismiss()
        }
        binding.btnDialogJoin.setOnClickListener {
            chatDialogAction.onClickJoinButton(chatInfo.cid)
            dismiss()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

interface ChatDialogAction {
    fun onClickJoinButton(cid: String)
    fun onClickCancleButton()
}