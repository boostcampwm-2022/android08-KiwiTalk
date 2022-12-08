package com.kiwi.kiwitalk.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentChatListBinding
import com.kiwi.kiwitalk.ui.chatting.ChattingActivity
import com.kiwi.kiwitalk.ui.search.SearchChatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory

@AndroidEntryPoint
class ChatListFragment : Fragment() {
    private val channelListViewModel: ChannelListViewModel
            by viewModels { ChannelListViewModelFactory() }
    private val chatListViewModel: ChatListViewModel by viewModels()

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initToolbar()

        binding.fabCreateChat.setOnClickListener {
            startActivity(Intent(requireContext(), SearchChatActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        val adapter = ChatListViewAdapter(object : ChatListViewAdapter.OnChatClickListener {
            override fun onChatClick(channel: Channel) {
                startActivity(ChattingActivity.createIntent(requireContext(), channel.cid))
            }

            override fun onChatLongClick(channel: Channel) {
                showExitChatDialog(channel.cid)
            }
        })

        channelListViewModel.state.observe(viewLifecycleOwner) {
            if (it.channels.isEmpty()) {
                binding.tvChatListEmpty.visibility = View.VISIBLE
                binding.rvChatList.visibility = View.INVISIBLE
            } else {
                binding.tvChatListEmpty.visibility = View.INVISIBLE
                binding.rvChatList.visibility = View.VISIBLE
                adapter.submitList(it.channels)
            }
        }
        binding.rvChatList.adapter = adapter
    }

    private fun initToolbar() {
        val navigation = Navigation.findNavController(binding.root)
        binding.chatListToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_chatList_logout -> {
                    Toast.makeText(requireContext(), "미구현 기능입니다.", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.item_chatList_actionToProffileSetting -> {
                    navigation.navigate(R.id.action_chatListFragment_to_profileSettingFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showExitChatDialog(cid: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("채팅방 나가기")
            .setMessage("채팅방을 나가시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                chatListViewModel.exitChat(cid)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }.create()
            .show()
    }
}