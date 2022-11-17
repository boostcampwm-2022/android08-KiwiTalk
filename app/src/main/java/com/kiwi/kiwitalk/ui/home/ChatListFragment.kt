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
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import io.getstream.chat.android.ui.message.MessageListActivity
import javax.inject.Inject

@AndroidEntryPoint
class ChatListFragment : Fragment() {
    private val TAG = this::class.simpleName

    @Inject
    lateinit var client: ChatClient

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

        binding.channelListView.setViewHolderFactory(ChatListItemViewHolderFactory())
        binding.channelListView.setChannelItemClickListener {
            startActivity(MessageListActivity.createIntent(requireContext(), it.cid))
        }

        val chatListViewModel: ChannelListViewModel by viewModels { ChannelListViewModelFactory() }
        chatListViewModel.bindView(binding.channelListView, this)
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