package com.kiwi.kiwitalk.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kiwi.kiwitalk.databinding.ActivityMainBinding
import com.kiwi.kiwitalk.ui.home.HomeActivity
import com.kiwi.kiwitalk.BuildConfig

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleApiClient: GoogleSignInClient
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val googleSignOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(SERVER_CLIENT_KEY)
//            .requestEmail()
//            .build()
//        googleApiClient = GoogleSignIn.getClient(this, googleSignOptions)
//
//        auth = FirebaseAuth.getInstance()
//        activityResultLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                if (it.resultCode == RESULT_OK) {
//                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(it.data)
//                    result ?: return@registerForActivityResult
//                    if (result.isSuccess) {
//                        val account = result.signInAccount
//                        resultLogin(account)
//                    }
//                }
//            }

//        binding.btnGoogleSignup.setOnClickListener {
//            val intent = googleApiClient.signInIntent
//            activityResultLauncher.launch(intent)
//        }
    }

//    private fun resultLogin(account: GoogleSignInAccount?) {
//        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) {
//                if (it.isSuccessful) {
//                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, HomeActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }

    companion object {
        const val SERVER_CLIENT_KEY = BuildConfig.SERVER_CLIENT_ID
    }
}

