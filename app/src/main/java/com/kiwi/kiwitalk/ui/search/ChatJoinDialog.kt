package com.kiwi.kiwitalk.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kiwi.domain.model.ChatInfo
import com.kiwi.kiwitalk.databinding.DialogJoinChatBinding

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
        _binding = DialogJoinChatBinding.inflate(inflater, container, false)
        binding.tvTemp.text = chatInfo.toString()
        binding.btnDialogClose.setOnClickListener { dismiss() }
        binding.btnDialogJoin.setOnClickListener {
            chatDialogAction.onClickJoinButton(chatInfo.cid)
            dismiss()
        }
//        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

interface ChatDialogAction {
    fun onClickJoinButton(cid: String)
}