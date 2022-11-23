package com.kiwi.kiwitalk.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.kiwi.domain.model.Marker
import kotlin.random.Random

data class ClusterMarker(
    val x: Double,
    val y: Double,
    private val _snippet: String?,
    private val _title: String?,
    val keywords: List<String>
) : ClusterItem {
    constructor(latLng: LatLng, snippet: String?, title: String?, keywords: List<String>) :
            this(latLng.latitude, latLng.longitude, snippet, title, keywords)

    private val _latLng = LatLng(x, y)
    override fun getPosition(): LatLng = _latLng
    override fun getSnippet(): String? = _snippet
    override fun getTitle(): String? = _title

    companion object {
        fun Marker.toClusterMarker(): ClusterMarker = ClusterMarker(
            x = this.x + Random.nextDouble(0.1),
            y = this.y,
            _snippet = cid,
            _title = toString(),
            keywords = keywords,
        )
    }
}