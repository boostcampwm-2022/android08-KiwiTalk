package com.kiwi.domain

interface UserUiCallback {
    fun onSuccess()
    fun onFailure(e: Throwable)
}