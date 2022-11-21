package com.kiwi.kiwitalk.ui.login

import android.os.Build
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
                        splashScreen.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        viewModel.idToken.observe(this) {
            doOnLoginInfoExist(it)
        }

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                try {
                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(it.data)
                    result ?: return@registerForActivityResult
                    Log.d(TAG, result.status.toString())
                    when(result.status.statusCode){
                        GoogleSignInStatusCodes.SUCCESS -> viewModel.signIn(result.signInAccount?.idToken ?: Const.EMPTY_STRING)
                        GoogleSignInStatusCodes.DEVELOPER_ERROR -> throw Exception("SHA키 등록 여부 확인")
                        12501 -> throw Exception("디바이스가 Google Play 서비스를 포함하는지 확인")
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.toString())
                }
            }

        binding.btnGoogleSignup.setOnClickListener {
            val intent = viewModel.googleApiClient.signInIntent
            activityResultLauncher.launch(intent)
        }
    }

    private fun doOnLoginInfoExist(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Snackbar.make(binding.root, LOGIN_SUCCESS, Snackbar.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    finishAffinity()
                    startActivity(intent)
                } else {
                    Snackbar.make(binding.root, LOGIN_FAIL, Snackbar.LENGTH_SHORT).show()
                }
            }
            .addOnCanceledListener(this) {
                Snackbar.make(binding.root, LOGIN_SERVER_ERROR, Snackbar.LENGTH_SHORT).show()
            }
    }

    companion object {
        const val TAG = "k001"
        const val LOGIN_SUCCESS = "로그인 성공"
        const val LOGIN_FAIL = "로그인 실패"
        const val LOGIN_SERVER_ERROR = "서버와의 연결이 불안정합니다"
    }
}