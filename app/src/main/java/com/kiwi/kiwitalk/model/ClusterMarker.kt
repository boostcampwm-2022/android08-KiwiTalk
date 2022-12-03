package com.kiwi.kiwitalk.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.kiwi.domain.model.Marker

data class ClusterMarker(
    val x: Double,
    val y: Double,
    private val markerSnippet: String?,
    private val markerTitle: String?,
    val keywords: List<String>,
    val cid: String
) : ClusterItem {
    constructor(
        latLng: LatLng, snippet: String?, title: String?, keywords: List<String>, cid: String
    ) : this(latLng.latitude, latLng.longitude, snippet, title, keywords, cid)

    private val latLng get() = LatLng(x, y)
    override fun getPosition(): LatLng = latLng
    override fun getSnippet(): String? = markerSnippet
    override fun getTitle(): String? = markerTitle

    companion object {
        fun Marker.toClusterMarker(): ClusterMarker = ClusterMarker(
            x = this.x,
            y = this.y,
            markerSnippet = cid,
            markerTitle = toString(),
            keywords = keywords,
            cid = cid
        )
    }
}