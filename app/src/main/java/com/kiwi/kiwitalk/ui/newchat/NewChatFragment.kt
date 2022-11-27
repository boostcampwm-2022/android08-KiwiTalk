package com.kiwi.kiwitalk.ui.newchat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentNewChatBinding
import com.kiwi.kiwitalk.ui.home.HomeActivity
import com.kiwi.kiwitalk.ui.newchat.SearchPlaceFragment.Companion.ADDRESS_KEY


class NewChatFragment : Fragment() {

    private var _binding : FragmentNewChatBinding? = null
    private val binding get() = checkNotNull(_binding)
    private var address: String? = null
    private val viewModel: SearchPlaceViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

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
        address = arguments?.getString(ADDRESS_KEY)

        if(address != null){
        findNavController().currentBackStackEntry
            ?.savedStateHandle?.apply {
                getLiveData<String>(ADDRESS_KEY).observe(viewLifecycleOwner) {
                    newChatViewModel.setAddress(it)
                }
                getLiveData<LatLng>(LATLNG_KEY).observe(viewLifecycleOwner) {
                    newChatViewModel.setLatLng(it)

                }
            }

            binding.tvChatSelectAddress.visibility = View.VISIBLE
            binding.tvChatSelectAddress.text = address
        }

        binding.btnNewChat.setOnClickListener {
            val intent = Intent(requireActivity(),HomeActivity::class.java)
            startActivity(intent)
        }

        binding.btcChatAddress.setOnClickListener {
            findNavController().navigate(R.id.action_newChatFragment_to_searchPlaceFragment)
        }
    }
    
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}