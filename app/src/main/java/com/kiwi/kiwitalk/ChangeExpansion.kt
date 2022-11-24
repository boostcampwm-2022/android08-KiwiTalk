package com.kiwi.kiwitalk

import android.location.Geocoder
import android.os.Build

object ChangeExpansion {
    @Suppress("DEPRECATION")
    fun Geocoder.changeLatLngToAddress(
        latitude: Double,
        longitude: Double,
        address: (android.location.Address?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getFromLocation(latitude, longitude, 1) { address(it.firstOrNull()) }
            return
        }
        try {
            address(getFromLocation(latitude, longitude, 1)?.firstOrNull())
        } catch(e: Exception) {
            //will catch if there is an internet problem
            address(null)
        }
    }
}