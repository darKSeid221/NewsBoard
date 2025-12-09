package com.robustrade.newsboard

import com.robustrade.newsboard.data.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query
interface NewsApiService {
    // Example using NewsAPI.org endpoints (you can swap provider)
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20,
        @Query("q") query: String? = null,
        @Query("country") country: String? = "us",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): NewsApiResponse
}