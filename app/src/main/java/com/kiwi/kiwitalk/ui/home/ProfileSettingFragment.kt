package com.kiwi.kiwitalk.ui.home

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentProfileSettingBinding
import com.kiwi.kiwitalk.ui.keyword.SearchKeywordViewModel
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter
import com.kiwi.kiwitalk.ui.setImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettingFragment : Fragment() {

    private var _binding: FragmentProfileSettingBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private val searchKeywordViewModel: SearchKeywordViewModel by activityViewModels()
    private val errorSnackbar: (message: String) -> Unit = {
        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
            .show()
    }
    lateinit var selectedKeywordAdapter: SelectedKeywordAdapter
    lateinit var backPressedCallback: OnBackPressedCallback

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            profileViewModel.setChatImage(
                it.data?.data?.toString() ?: return@registerForActivityResult
            )
        }
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
        setAdapter()
        setViewModelObserve()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                popBackStackWithNoSave()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()

        backPressedCallback.remove()
    }

    private fun setListener() {
        binding.tvProfileChangeSelectKeyword.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_profileSettingFragment_to_searchKeywordFragment)
        }

        with(binding.toolbarProfileTitle) {
            setNavigationOnClickListener {
                popBackStackWithNoSave()
            }

            setOnMenuItemClickListener {
                profileViewModel.checkInput()
                return@setOnMenuItemClickListener false
            }
        }

        binding.btnProfileAddImage.setOnClickListener {
            activityResultLauncher.launch(Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            })
        }

        binding.etProfileDescription.doAfterTextChanged {
            it?.toString()?.let { desString ->
                if (desString.contains("\n")) {
                    binding.etProfileDescription.setText(desString.replace("\n", ""))
                    binding.etProfileDescription.clearFocus()
                    (activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(binding.root.windowToken, 0)
                }
            }
        }
    }

    private fun setAdapter() {
        selectedKeywordAdapter = SelectedKeywordAdapter()
        binding.rvProfileSelectKeywordList.adapter = selectedKeywordAdapter
    }

    private fun setViewModelObserve() {
        searchKeywordViewModel.selectedKeyword.observe(viewLifecycleOwner) {
            selectedKeywordAdapter.submitList(it)
        }

        profileViewModel.myKeywords.value?.let { list ->
            if (searchKeywordViewModel.selectedKeyword.value == null) {
                list.forEach {
                    searchKeywordViewModel.setSelectedKeywords(it.name)
                }
            }
        }

        profileViewModel.profileImage.observe(viewLifecycleOwner) {
            setImage(binding.ivProfileImage, it)
        }

        profileViewModel.isAllConditionPass.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { boolean ->
                if (boolean) {
                    try {
                        profileViewModel.setMySelectedKeyword(searchKeywordViewModel.selectedKeyword.value)
                        profileViewModel.setUpdateProfile()
                        this@ProfileSettingFragment.findNavController().popBackStack()
                    } catch (e: Exception) {
                        Log.d("NAV_PROFILE", "setListener: $e")
                        errorSnackbar("저장을 실행할 수 없습니다. 앱을 종료해주세요.")
                    }
                } else {
                    errorSnackbar("입력양식이 올바르지 않습니다.")
                }
            }
        }
    }

    private fun popBackStackWithNoSave() {
        try {
            restoreSelectedKeywordFromCurrentUser()
            this@ProfileSettingFragment.findNavController().popBackStack()
        } catch (e: Exception) {
            Log.d("NAV_PROFILE", "setListener: $e")
            errorSnackbar("뒤로가기를 실행할 수 없습니다. 앱을 종료해주세요.")
        }
    }

    private fun restoreSelectedKeywordFromCurrentUser() {
        searchKeywordViewModel.deleteAllSelectedKeyword()
        profileViewModel.myKeywords.value?.let { list ->
            list.forEach {
                searchKeywordViewModel.setSelectedKeywords(it.name)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}