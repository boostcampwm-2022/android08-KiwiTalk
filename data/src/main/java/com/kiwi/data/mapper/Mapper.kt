package com.kiwi.data.mapper

import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.domain.model.Marker

object Mapper {
    fun MarkerRemote.toMarker() = Marker(
        cid = cid,
        x = x,
        y = y,
        keywords = keywords
    )
}