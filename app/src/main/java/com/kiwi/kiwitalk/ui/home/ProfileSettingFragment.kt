package com.kiwi.kiwitalk.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentProfileSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettingFragment : Fragment() {
    lateinit var binding: FragmentProfileSettingBinding
    val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileSettingBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = profileViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }


    fun setListener(){
        with(binding.tbProfileTitle){
            setNavigationOnClickListener {
                try {
                    this@ProfileSettingFragment.findNavController().popBackStack()
                } catch (e: Exception) {
                    Log.d("NAV_PROFILE", "setListener: $e")
                }
            }

            setOnMenuItemClickListener {
                try {
                    this@ProfileSettingFragment.findNavController().popBackStack()
                } catch (e: Exception) {
                    Log.d("NAV_PROFILE", "setListener: $e")
                }
                return@setOnMenuItemClickListener true
            }
        }
    }
}