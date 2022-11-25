package com.kiwi.kiwitalk.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.material.snackbar.Snackbar
import com.kiwi.kiwitalk.Const
import com.kiwi.kiwitalk.databinding.ActivityLoginBinding
import com.kiwi.kiwitalk.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val splashScreen: View = findViewById(android.R.id.content)
        splashScreen.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.isReady) {
                        if (!viewModel.isNetworkConnect) {
                            showPopUpMessage(NO_NETWORK)
                        }
                        splashScreen.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        skipLoginWhenTokenExist(viewModel.userId.value)

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                loginWithGoogleCredential(
                    Auth.GoogleSignInApi.getSignInResultFromIntent(it.data)
                )
            }

        binding.btnGoogleSignup.setOnClickListener {
            val intent = viewModel.googleApiClient.signInIntent
            activityResultLauncher.launch(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun skipLoginWhenTokenExist(token: String?) {
        if (!token.isNullOrEmpty()) {
            loginWithLocalToken()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loginWithGoogleCredential(result: GoogleSignInResult?) {
        try {
            result ?: return
            Log.d(TAG, result.status.toString())

            when (result.status.statusCode) {
                GoogleSignInStatusCodes.SUCCESS -> result.signInAccount?.apply {
                    viewModel.saveToken(
                        id = id!!,
                        name = displayName ?: Const.EMPTY_STRING,
                        imageUrl = photoUrl.toString()
                    )
                }
                GoogleSignInStatusCodes.DEVELOPER_ERROR -> throw Exception("SHA키 등록 여부 확인")
                GoogleSignInStatusCodes.NETWORK_ERROR -> showPopUpMessage(NO_NETWORK)
                12501 -> throw Exception("디바이스가 Google Play 서비스를 포함하는지 확인")
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }

        if (!viewModel.userId.value.isNullOrEmpty()) {
            loginWithLocalToken()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loginWithLocalToken() {
        viewModel.loginWithLocalToken()
        showPopUpMessage(LOGIN_SUCCESS)
        navigateToHome()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        finishAffinity()
        startActivity(intent)
    }

    private fun showPopUpMessage(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "k001"
        private const val LOGIN_SUCCESS = "로그인 성공"
        private const val NO_NETWORK = "인터넷 연결을 확인해주세요"
    }
}