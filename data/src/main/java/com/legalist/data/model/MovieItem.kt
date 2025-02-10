package com.legalist.data.model

import com.legalist.data.util.Constant

data class MovieItem(
    val id: Int,
    val overview: String,
    val backdrop_path: String,
    val title: String,
    val vote_average: Double,
    val genre_ids: List<Int>

){
    val imageUrl: String
        get() = Constant.IMAGE_BASE_URL+backdrop_path
}