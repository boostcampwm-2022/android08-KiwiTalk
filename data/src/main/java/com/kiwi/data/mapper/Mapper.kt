package com.kiwi.data.mapper

import com.kiwi.data.model.remote.MarkerRemote
import com.kiwi.data.model.remote.PlaceListRemote
import com.kiwi.data.model.remote.PlaceRemote
import com.kiwi.domain.model.Marker
import com.kiwi.domain.model.Place
import com.kiwi.domain.model.PlaceList

object Mapper {
    fun MarkerRemote.toMarker() = Marker(
        cid = cid,
        x = x,
        y = y,
        keywords = keywords
    )

    fun PlaceListRemote.toPlaceList() : PlaceList {
        val result = mutableListOf<Place>()
        documents.mapIndexed { index, placeRemote ->
            result.add(index,placeRemote.toPlace())
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