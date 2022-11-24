package com.kiwi.kiwitalk.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kiwi.kiwitalk.databinding.FragmentChatListBinding
import com.kiwi.kiwitalk.ui.search.SearchChatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import io.getstream.chat.android.ui.message.MessageListActivity
import javax.inject.Inject

@AndroidEntryPoint
class ChatListFragment : Fragment() {
    private val TAG = this::class.simpleName
    private val chatListViewModel: ChannelListViewModel by viewModels { ChannelListViewModelFactory() }

    @Inject
    lateinit var client: ChatClient // 임시

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

        binding.fabCreateChat.setOnClickListener {
            startActivity(Intent(requireContext(), SearchChatActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        val adapter = ChatListViewAdapter()
        adapter.onClickListener = object : ChatListViewAdapter.OnChatClickListener {
            override fun onChatClick(channel: Channel) {
                startActivity(MessageListActivity.createIntent(requireContext(), channel.cid))
            }
        }

        chatListViewModel.state.observe(viewLifecycleOwner) {
            if (it.channels.isEmpty()) {
                binding.tvChatListEmpty.visibility = View.VISIBLE
                binding.rvChatList.visibility = View.INVISIBLE
            } else {
                binding.tvChatListEmpty.visibility = View.INVISIBLE
                binding.rvChatList.visibility = View.VISIBLE
                adapter.submitList(it.channels)
            }
        }
        binding.rvChatList.apply {
            this.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}