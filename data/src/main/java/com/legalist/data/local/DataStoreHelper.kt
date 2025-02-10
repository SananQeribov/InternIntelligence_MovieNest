package com.legalist.data.local

import android.content.Context
import android.graphics.Movie
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.legalist.data.model.Category
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// Define the data store property
val Context.dataStore by preferencesDataStore(name = "movie_data_store")

class DataStoreHelper(context: Context) {

    // Access the dataStore property
    private val dataStore = context.dataStore

    companion object {
        private val MOVIE_LIST_KEY = stringPreferencesKey("movie_list")
    }

    // Filmləri saxla
    suspend fun saveMovies(movies: List<Category>) {
        val json = Gson().toJson(movies)
        dataStore.edit{ preferences ->
            preferences[MOVIE_LIST_KEY] = json
        }
    }

    // Filmləri oxu
    val moviesFlow: Flow<List<Category>> = dataStore.data
        .map { preferences ->
            val json = preferences[MOVIE_LIST_KEY] ?: "[]"
            val type = object : TypeToken<List<Category>>() {}.type
            Gson().fromJson(json, type)
        }
}
