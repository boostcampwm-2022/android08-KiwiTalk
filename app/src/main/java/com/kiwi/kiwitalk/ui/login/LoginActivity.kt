package com.kiwi.kiwitalk.ui.login

import android.os.Build
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kiwi.kiwitalk.BuildConfig
import com.kiwi.kiwitalk.Const
import com.kiwi.kiwitalk.databinding.ActivityLoginBinding
import com.kiwi.kiwitalk.databinding.ActivityMainBinding
import com.kiwi.kiwitalk.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleApiClient: GoogleSignInClient
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manageSplashScreen: View = findViewById(android.R.id.content)
        manageSplashScreen.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.isReady) {
                        manageSplashScreen.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        Log.d("k001", "리스너 이런거 초기화")
        val googleSignOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(SERVER_CLIENT_KEY)
            .requestEmail()
            .build()
        googleApiClient = GoogleSignIn.getClient(this, googleSignOptions)

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    Log.d("k001", "activityResultLauncher에서 RESULT_OK")
                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(it.data)
                    result ?: return@registerForActivityResult
                    if (result.isSuccess) {
                        Log.d("k001", "result 성공")
                        val account = result.signInAccount
                        viewModel.signIn(account?.idToken?: Const.EMPTY_STRING)
                    }
                } else {
                    Log.d("k001", "activityResultLauncher에서 RESULT_OK가 아님")
                    Log.d("k001", it.resultCode.toString())
                }
            }

        binding.btnGoogleSignup.setOnClickListener {
            Log.d("k001", "버튼 클릭")
            val intent = googleApiClient.signInIntent
            activityResultLauncher.launch(intent)
        }

        viewModel.idToken.observe(this) {
            doOnLoginInfoExist(it)
        }
        Log.d("k001", "초기화 끝남")
    }

    private fun doOnLoginInfoExist(token: String) {
        Log.d("k001", "옵저빙된 토큰 값 : $token")
        
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d("k001", "로그인 성공")
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, HomeActivity::class.java)
//                    startActivity(intent)
                } else {
                    Log.d("k001", "로그인 실패")
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        const val SERVER_CLIENT_KEY = BuildConfig.SERVER_CLIENT_ID
    }
}