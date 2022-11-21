package com.kiwi.kiwitalk.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kiwi.kiwitalk.AppPreference
import com.kiwi.kiwitalk.BuildConfig
import com.kiwi.kiwitalk.Const
import com.kiwi.kiwitalk.ui.login.LoginActivity
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
    fun providePreference(@ApplicationContext context: Context): AppPreference
            = AppPreference(context.getSharedPreferences(Const.LOGIN_HISTORY_KEY, Context.MODE_PRIVATE))

    @Singleton
    @Provides
    fun provideGoogleApiClient(@ApplicationContext context: Context): GoogleSignInClient {
        val googleSignOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.SERVER_CLIENT_ID)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, googleSignOptions)
    }
}