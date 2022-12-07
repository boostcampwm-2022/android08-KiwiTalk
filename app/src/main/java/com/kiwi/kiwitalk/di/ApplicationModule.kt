package com.kiwi.kiwitalk.di

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kiwi.data.AppPreference
import com.kiwi.kiwitalk.util.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Singleton
    @Provides
    fun providePreference(@ApplicationContext context: Context): AppPreference =
        AppPreference(context.getSharedPreferences(Const.LOGIN_ID_KEY, Context.MODE_PRIVATE))

    @Singleton
    @Provides
    fun provideGoogleApiClient(@ApplicationContext context: Context): GoogleSignInClient {
        val googleSignOptions: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("611516619499-v3b7482t6qbldpn0ens6rgp45i6fo577.apps.googleusercontent.com")
                .requestEmail()
                .build()
        return GoogleSignIn.getClient(context, googleSignOptions)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Singleton
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(ConnectivityManager::class.java)

}