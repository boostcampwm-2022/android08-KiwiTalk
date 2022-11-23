package com.kiwi.kiwitalk.ui.newchat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentNewChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewChatFragment : Fragment() {

    private var _binding : FragmentNewChatBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val searchPlaceViewModel: SearchPlaceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChatPlace.setOnClickListener {
            findNavController().navigate(R.id.action_newChatFragment_to_searchPlaceFragment)
        }
    }
    
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}