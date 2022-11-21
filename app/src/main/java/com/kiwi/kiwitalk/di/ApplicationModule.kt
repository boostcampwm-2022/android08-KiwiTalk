package com.kiwi.kiwitalk.di

import android.content.Context
import com.kiwi.kiwitalk.AppPreference
import com.kiwi.kiwitalk.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Singleton
    @Provides
    fun providePreference(@ApplicationContext context: Context): AppPreference
        = AppPreference(context.getSharedPreferences(Const.LOGIN_HISTORY_KEY, Context.MODE_PRIVATE))

}