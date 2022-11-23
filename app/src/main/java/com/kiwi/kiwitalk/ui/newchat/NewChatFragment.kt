package com.kiwi.kiwitalk.ui.newchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentNewChatBinding


class NewChatFragment : Fragment() {

    private var _binding : FragmentNewChatBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewChatBinding.inflate(inflater,container,false)
        return _binding?.root
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