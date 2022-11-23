package com.kiwi.kiwitalk.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.kiwi.domain.model.Marker

data class ClusterMarker(
    private val _latLng: LatLng,
    private val _snippet: String?,
    private val _title: String?
) : ClusterItem {
    val x = _latLng.latitude
    val y = _latLng.longitude
    override fun getPosition(): LatLng = _latLng
    override fun getSnippet(): String? = _snippet
    override fun getTitle(): String? = _title

    constructor(x: Double, y: Double, snippet: String?, title: String?) :
            this(LatLng(x, y), snippet, title)

    companion object {
        fun Marker.toMyItem(): ClusterMarker = ClusterMarker(
            x = this.x,
            y = this.y,
            toString(),
            cid
        )
    }
}