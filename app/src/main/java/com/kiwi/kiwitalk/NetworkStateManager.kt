package com.kiwi.kiwitalk

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.Toast
import android.widget.Toast.makeText


class NetworkStateManager(context: Context) : ConnectivityManager.NetworkCallback() {

    private var context : Context? = null
    private var networkRequest: NetworkRequest? = null
    private var connectivityManager : ConnectivityManager? = null

    init {
        this.context = context
        this.networkRequest =
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()
    }

    fun register() {
        connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager!!.registerNetworkCallback(networkRequest!!, this)
    }

    fun unregister() {
        connectivityManager!!.unregisterNetworkCallback(this)
    }

    private fun networkShowDialog() {
        val msgBuilder= AlertDialog.Builder(context)
            .setTitle("Network Connect Check")
            .setMessage("네트워크 상태를 확인해주세요.")
            .setPositiveButton("재시도") { dialogInterface, i ->

            }
            .setNegativeButton("취소") { dialogInterface, i ->

            }
            .setCancelable(false)
        val msgDlg: AlertDialog = msgBuilder.create()
        msgDlg.show()
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        
        makeText(context, "network available", Toast.LENGTH_SHORT).show()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkShowDialog()
    }

}