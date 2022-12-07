package com.kiwi.data.datasource.local

import com.kiwi.data.AppPreference
import com.kiwi.data.Const
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val pref: AppPreference
) : UserLocalDataSource {
    override suspend fun saveToken(id: String?, name: String?, imageUrl: String?) {
        id?.let { pref.setString(Const.LOGIN_ID_KEY, it) }
        name?.let { pref.setString(Const.LOGIN_NAME_KEY, it) }
        imageUrl?.let { pref.setString(Const.LOGIN_URL_KEY, it) }
    }

    override suspend fun getToken(): String {
        return pref.getString(
            Const.LOGIN_ID_KEY, Const.EMPTY_STRING
        )
    }

    override fun isValidToken(token: String): Boolean {
        return tokenRegex.matches(token)
    }

    companion object {
        private const val LOCATION = "k001"
        private val tokenRegex = Regex("[0-9,a-z]{1,21}")
    }
}