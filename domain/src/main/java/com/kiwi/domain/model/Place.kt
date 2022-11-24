package com.kiwi.domain.model

data class Place(
    var placeName: String, // 장소명, 업체명
    var addressName: String, // 전체 지번 주소
    var roadAddressName: String, // 전체 도로명 주소
    var lng: String, // X 좌표값 혹은 longitude
    var lat: String, // Y 좌표값 혹은 latitude
)