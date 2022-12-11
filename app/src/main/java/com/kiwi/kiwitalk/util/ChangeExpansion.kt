package com.kiwi.kiwitalk.util

import android.location.Geocoder

object ChangeExpansion {
    fun Geocoder.changeLatLngToAddress(
        latitude: Double,
        longitude: Double,
        address: (android.location.Address?) -> Unit
    ) {
        try {
            @Suppress("DEPRECATION")
            address(getFromLocation(latitude, longitude, 1)?.firstOrNull())
        } catch (e: Exception) {
            address(null)
        }
    }
}