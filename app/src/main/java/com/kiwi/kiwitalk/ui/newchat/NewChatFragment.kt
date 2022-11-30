package com.kiwi.kiwitalk.ui.newchat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.kiwi.domain.model.NewChat
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.databinding.FragmentNewChatBinding
import com.kiwi.kiwitalk.ui.home.HomeActivity
import com.kiwi.kiwitalk.ui.newchat.SearchPlaceFragment.Companion.ADDRESS_KEY
import com.kiwi.kiwitalk.ui.newchat.SearchPlaceFragment.Companion.LATLNG_KEY
import com.kiwi.kiwitalk.ui.setImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewChatFragment : Fragment() {

    private var _binding: FragmentNewChatBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val newChatViewModel: NewChatViewModel by viewModels()

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
                addNewChat(it,System.currentTimeMillis().toString(), newChatInfo.value ?: return@observe)
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
            btnNewChat.setOnClickListener {
                if (allCheckNull()) {
                    newChatViewModel.setNewChat(changeChatInfo())
                }
            }
            btnChatKeyword.setOnClickListener {
                findNavController().navigate(R.id.action_newChatFragment_to_searchKeywordFragment)
            }
        }
    }

    private fun changeChatInfo(): NewChat {
        with(newChatViewModel) {
            return NewChat(
                chatImage.value ?: "",
                binding.etChatName.text.toString(),
                binding.etChatDescription.text.toString(),
                binding.etChatMaxPersonnel.text.toString(),
                listOf("운동", "카페"),

                address.value ?: "",

                latLng.value?.latitude ?: 0.0,
                latLng.value?.longitude ?: 0.0
            )
        }
    }

    private fun allCheckNull(): Boolean {
        with(binding) {
            if(etChatName.checkNull()) return false
            if(etChatDescription.checkNull()) return false
            if(etChatMaxPersonnel.checkNull()) return false
            if(tvChatSelectAddress.checkNull()) return false
        }
        return true
    }

    private fun EditText.checkNull(): Boolean {
        if (this.text.toString() == "") {
            this.requestFocus()
            return true
        }
        return false
    }

    private fun TextView.checkNull(): Boolean {
        if (this.text.toString() == "") {
            return true
        }
        return false
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}