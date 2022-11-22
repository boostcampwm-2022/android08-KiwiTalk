package com.kiwi.kiwitalk.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kiwi.kiwitalk.databinding.FragmentChatListBinding
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User
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
        initUser()
        initRecyclerView()
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

    private fun initUser() {
        val user = User(
            id = "kiwi",
            name = "Wi Ki",
            image = "https://bit.ly/2TIt8NR",
        )
        val token = client.devToken(user.id)
        if (client.getCurrentUser() == null) {
            client.connectUser(user, token).enqueue { result ->
                Log.d(TAG, "result is ${result.isSuccess}: $result")
            }
        } else {
            Log.d(TAG, "user: ${client.getCurrentUser()}")
        }
    }
}