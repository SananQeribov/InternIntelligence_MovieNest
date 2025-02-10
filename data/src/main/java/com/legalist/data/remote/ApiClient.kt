package com.legalist.data.remote

import com.legalist.data.util.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
object ApiClient {
    fun getClient(): ApiService {
        // Logging interceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // API açarını əlavə edən interceptor
        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .addHeader( Constant.bear_token,Constant.API_KEY)
                .build()
            chain.proceed(newRequest)
        }

        // OkHttpClient konfiqurasiyası
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Logging interceptor əlavə et
            .addInterceptor(apiKeyInterceptor) // API açarı interceptoru əlavə et
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        // Retrofit müştərisini qurmaq
        return Retrofit.Builder()
            .baseUrl(Constant.baseUrl) // Əsas URL
            .addConverterFactory(GsonConverterFactory.create()) // Gson konvertor
            .client(okHttpClient) // Müştərini təyin et
            .build()
            .create(ApiService::class.java) // `ApiService` interfeysini yarat
    }
}

