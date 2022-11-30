package com.kiwi.kiwitalk.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kiwi.kiwitalk.databinding.FragmentProfileSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettingFragment : Fragment() {

    private var _binding: FragmentProfileSettingBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    val errorSnackbar: () -> Unit = {
        Snackbar.make(binding.root, "뒤로가기를 실행할 수 없습니다. 앱을 종료해주세요.", Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileSettingBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = profileViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }

    fun setListener() {
        with(binding.toolbarProfileTitle) {
            setNavigationOnClickListener {
                try {
                    this@ProfileSettingFragment.findNavController().popBackStack()
                } catch (e: Exception) {
                    Log.d("NAV_PROFILE", "setListener: $e")
                    errorSnackbar
                }
            }

            setOnMenuItemClickListener {
                try {
                    profileViewModel.setUpdateProfile()
                    this@ProfileSettingFragment.findNavController().popBackStack()
                    return@setOnMenuItemClickListener true
                } catch (e: Exception) {
                    Log.d("NAV_PROFILE", "setListener: $e")
                    errorSnackbar
                }
                return@setOnMenuItemClickListener false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}