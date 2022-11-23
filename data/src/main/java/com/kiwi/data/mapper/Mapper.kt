package com.kiwi.data.mapper

import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.data.model.remote.PlaceListRemote
import com.kiwi.data.model.remote.PlaceRemote
import com.kiwi.domain.model.PlaceList
import com.kiwi.domain.model.Place
import com.kiwi.domain.model.Marker

object Mapper {
    fun MarkerRemote.toMarker() = Marker(
        cid = cid,
        x = x,
        y = y,
        keywords = keywords
    )


    fun PlaceListRemote.toPlaceList() : PlaceList {
        var cnt = 0
        val result = mutableListOf<Place>()
        documents.forEach {
            result.add(cnt,it.toPlace())
            cnt++
        }

        return PlaceList(list = result)
    }

    fun PlaceRemote.toPlace() = Place(
        placeName = place_name,
        addressName = address_name,
        roadAddressName = road_address_name,
        lng = x,
        lat =y
    )
}