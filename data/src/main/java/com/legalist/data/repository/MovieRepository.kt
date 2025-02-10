package com.legalist.data.repository

import com.legalist.data.model.MovieResponse
import com.legalist.data.remote.ApiService
import com.legalist.data.util.Constant
import com.legalist.data.util.Resource
class MovieRepository(private val apiService: ApiService) {

    suspend fun getMovieData(): Resource<MovieResponse> {
        return try {
            val response = apiService.getMovieData(token = Constant.bear_token)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error("Empty response body")
                }
            } else {
                Resource.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.localizedMessage ?: "An unknown error occurred"}")
        }
    }
}
