package com.kiwi.kiwitalk.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.model.ClusterMarker
import com.kiwi.kiwitalk.util.Util.createDrawableFromView

class ClusterMarkerRenderer(val context: Context, map: GoogleMap, clusterManager: ClusterManager<ClusterMarker>)
    : DefaultClusterRenderer<ClusterMarker>(context, map, clusterManager) {


    override fun onBeforeClusterItemRendered(item: ClusterMarker, markerOptions: MarkerOptions) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_marker, null)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(view)))
    }

    override fun onClusterRendered(cluster: Cluster<ClusterMarker>, marker: Marker) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_marker, null)
        val markerCount = view.findViewById<TextView>(R.id.tv_marker_count)
        markerCount.text = cluster.size.toString()
        markerCount.visibility = View.VISIBLE
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(view)))
    }

    override fun shouldRenderAsCluster(cluster: Cluster<ClusterMarker>): Boolean {
        return cluster.size > 1
    }
}
