package com.kiwi.domain.model

data class KeywordCategory(
    var name: String,
    var keywords: MutableList<Keyword>
)
