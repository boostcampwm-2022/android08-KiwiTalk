package com.kiwi.kiwitalk

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest



class NetworkStateManager(context: Context) : ConnectivityManager.NetworkCallback() {

    private var context : Context? = null
    private var networkRequest: NetworkRequest? = null
    private var connectivityManager : ConnectivityManager? = null
    private val msgBuilder: AlertDialog.Builder
    private val msgDlg: AlertDialog

    init {
        this.context = context
        this.networkRequest =
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()
        this.msgBuilder = AlertDialog.Builder(context)
            .setTitle("Network State Error")
            .setMessage("네트워크 상태를 확인해주세요.")
            .setPositiveButton("취소") { _, _ ->
            }
            .setCancelable(false)
        this.msgDlg = msgBuilder.create()
    }

    fun register() {
        connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager?.registerNetworkCallback(networkRequest?:return, this)
    }

    fun unregister() {
        connectivityManager?.unregisterNetworkCallback(this)
        msgDlg.dismiss()
    }

    private fun networkShowDialog() {
        msgDlg.show()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkShowDialog()
    }


}