package com.kiwi.kiwitalk.di

import android.content.Context
import com.kiwi.kiwitalk.BuildConfig.STREAM_API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChatModule {
    @Provides
    @Singleton
    fun provideChatClient(@ApplicationContext context: Context): ChatClient {
        return ChatClient.Builder(STREAM_API_KEY, context)
            .withPlugin(getOfflinePlugin(context))
            .logLevel(ChatLogLevel.ALL)
            .build()
    }

    private fun getOfflinePlugin(@ApplicationContext context: Context) =
        StreamOfflinePluginFactory(
            config = Config(),
            appContext = context,
        )
}