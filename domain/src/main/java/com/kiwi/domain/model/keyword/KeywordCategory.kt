package com.kiwi.domain.model.keyword

data class KeywordCategory(
    var name: String,
    var keywords: MutableList<Keyword>
)
