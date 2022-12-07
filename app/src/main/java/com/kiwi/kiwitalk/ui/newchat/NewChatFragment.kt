package com.kiwi.kiwitalk.ui.newchat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.kiwi.domain.model.NewChatInfo
import com.kiwi.kiwitalk.NetworkStateManager
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentNewChatBinding
import com.kiwi.kiwitalk.ui.home.HomeActivity
import com.kiwi.kiwitalk.ui.keyword.SearchKeywordViewModel
import com.kiwi.kiwitalk.ui.keyword.recyclerview.SelectedKeywordAdapter
import com.kiwi.kiwitalk.ui.newchat.SearchPlaceFragment.Companion.ADDRESS_KEY
import com.kiwi.kiwitalk.ui.newchat.SearchPlaceFragment.Companion.LATLNG_KEY
import com.kiwi.kiwitalk.ui.setImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewChatFragment : Fragment() {

    private var _binding: FragmentNewChatBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val newChatViewModel: NewChatViewModel by viewModels()
    private val searchKeywordViewModel: SearchKeywordViewModel by activityViewModels()
    private lateinit var networkConnectionState: NetworkStateManager

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            newChatViewModel.setChatImage(
                it.data?.data?.toString() ?: return@registerForActivityResult
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkConnectionState = NetworkStateManager(requireContext())
        networkConnectionState.register()

        findNavController().currentBackStackEntry
            ?.savedStateHandle?.apply {
                getLiveData<String>(ADDRESS_KEY).observe(viewLifecycleOwner) {
                    newChatViewModel.setAddress(it)
                }
                getLiveData<LatLng>(LATLNG_KEY).observe(viewLifecycleOwner) {
                    newChatViewModel.setLatLng(it)
                }
            }

        with(newChatViewModel) {
            address.observe(viewLifecycleOwner) {
                binding.tvChatSelectAddress.visibility = View.VISIBLE
                binding.tvChatSelectAddress.text = it
            }

            chatImage.observe(viewLifecycleOwner) {
                setImage(binding.ivChatImage, it)
            }

            newChatInfo.observe(viewLifecycleOwner) {
                newChatViewModel.setChatId()
            }

            chatId.observe(viewLifecycleOwner) {
                addNewChat(
                    it,
                    System.currentTimeMillis().toString(),
                    newChatInfo.value ?: return@observe
                )
                val intent = Intent(requireActivity(), HomeActivity::class.java)
                startActivity(intent)

                requireActivity().finish()
            }
        }

        with(binding) {
            btnChatAddress.setOnClickListener {
                findNavController().navigate(R.id.action_newChatFragment_to_searchPlaceFragment)
            }
            btnChatAddImage.setOnClickListener {
                activityResultLauncher.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                })
            }
            btnChatKeyword.setOnClickListener {
                findNavController().navigate(R.id.action_newChatFragment_to_searchKeywordFragment)
            }
        }

        initToolbar()
        initKeywordRecyclerView()
    }

    private fun initKeywordRecyclerView() {
        val adapter = SelectedKeywordAdapter()
        adapter.submitList(searchKeywordViewModel.selectedKeyword.value)
        binding.rvNewChatKeywords.adapter = adapter
    }

    private fun initToolbar() {
        binding.newChatMapToolbar.setNavigationOnClickListener {
            activity?.finish()
        }
        binding.newChatMapToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_action_new_chat_save -> {
                    val keywords = searchKeywordViewModel.selectedKeyword.value?.map { keyword -> keyword.name }
                    if (allCheckNull() && checkKeyword(keywords)) {
                        newChatViewModel.setNewChat(changeChatInfo(checkNotNull(keywords)))
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun changeChatInfo(keywords: List<String>): NewChatInfo {
        with(newChatViewModel) {
            return NewChatInfo(
                chatImage.value ?: "",
                binding.etChatName.text.toString(),
                binding.etChatDescription.text.toString(),
                binding.etChatMaxPersonnel.text.toString(),
                keywords,
                address.value ?: "",
                latLng.value?.latitude ?: 0.0,
                latLng.value?.longitude ?: 0.0
            )
        }
    }

    private fun checkKeyword(keywords: List<String>?): Boolean {
        return if(keywords != null && keywords.isEmpty().not()){
            true
        } else {
            Toast.makeText(requireContext(),"키워드를 선택해 주세요.",Toast.LENGTH_SHORT).show()
            //Snackbar.make(this,"키워드를 선택해 주세요.", Snackbar.LENGTH_SHORT).show()
            false
        }
    }

    private fun allCheckNull(): Boolean {
        with(binding) {
            if (etChatName.checkNull().not()) return false
            if (etChatDescription.checkNull().not()) return false
            if (etChatMaxPersonnel.checkNull().not() || etChatMaxPersonnel.checkMaxMember().not()) {
                return false
            }
            if (tvChatSelectAddress.checkNull().not()) return false
        }
        return true
    }

    private fun EditText.checkMaxMember(): Boolean {
        val cnt = this.text.toString().toInt()
        if(cnt > 100) {
            Snackbar.make(this,"최대 인원 수를 초과했습니다.", Snackbar.LENGTH_SHORT).show()
            this.requestFocus()
            return false
        }
        return true
    }

    private fun EditText.checkNull(): Boolean {
        if (this.text.toString() == "") {
            this.requestFocus()
            return false
        }
        return true
    }

    private fun TextView.checkNull(): Boolean {
        if (this.text.toString() == "") {
            Snackbar.make(this,"장소를 선택해 주세요", Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroy() {
        _binding = null
        networkConnectionState.unregister()
        super.onDestroy()
    }
}