package com.legalist.movienest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.legalist.data.model.MovieItem
import com.legalist.data.model.Upcominglist
import com.legalist.data.remote.ApiClient
import com.legalist.data.util.Constant
import com.legalist.data.util.Resource
import kotlinx.coroutines.launch

class UpcomingViewModel:ViewModel() {
    private val _upcomingList = MutableLiveData<Resource<List<Upcominglist>>>()
    val upcoming: LiveData<Resource<List<Upcominglist>>> get() = _upcomingList
    val isLoading = MutableLiveData(false)
    val errorMessage: MutableLiveData<String?> = MutableLiveData()
    fun getMovieList() {
        viewModelScope.launch {
            // _movieList.postValue(Resource.Loading())  // show loading state

            isLoading.value = true
            try {
                val response = ApiClient.getClient().getUpcomingData(token = Constant.bear_token)
                Log.e("data  come in  viewmodel ","${response.body()}")
                val resource = when {
                    response.isSuccessful -> {

                        val movieItem = response.body()?.results

                        _upcomingList.postValue(Resource.Success(movieItem))


                        when {
                            movieItem != null -> Resource.Success(movieItem)

                            else -> {
                                Resource.Error("Empty Response Body")
                            }
                        }
                    }

                    else -> {
                        Resource.Error(response.message() ?: "An unknown error occurred")
                    }
                }


            } catch (e:Exception) {
                _upcomingList.postValue(Resource.Error("Exception: ${e.localizedMessage ?: "An unknown error occurred"}"))
            }
            finally {
                isLoading.value = false
            }
        }
    }


}