package com.legalist.data.remote

import com.legalist.data.model.Details
import com.legalist.data.model.MovieResponse
import com.legalist.data.model.UpcomingItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET("popular?api_key=ec8956b8b9a8bf7f22c46705f6f587ba")
    suspend fun getMovieData(@Header("Authorization") token: String): Response<MovieResponse>

    @GET("upcoming?api_key=ec8956b8b9a8bf7f22c46705f6f587ba")
    suspend fun getUpcomingData(@Header("Authorization") token: String): Response<UpcomingItem>

    @GET("{movieId}?api_key=ec8956b8b9a8bf7f22c46705f6f587ba")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: String,
        @Header("Authorization") token: String
    ): Response<Details>
}