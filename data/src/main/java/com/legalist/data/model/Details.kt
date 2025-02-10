package com.legalist.data.model

import com.legalist.data.util.Constant

data class Details(
    val title:String,
    val backdrop_path: String,
    val overview: String,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val spoken_languages: List<SpokenLanguage>,
    val vote_average: Double,

){val imageUrl: String
    get() = Constant.IMAGE_BASE_URL+backdrop_path}