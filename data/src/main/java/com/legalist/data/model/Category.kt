package com.legalist.data.model

data class Category(
    val title: String ,
    val imageUrl: Int,
    val videoId : String,
    var isExpandable: Boolean = false

)
