package com.byteberserker.newsboard.data.remote

import com.byteberserker.newsboard.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query
interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20,
        @Query("q") query: String? = null,
        @Query("country") country: String? = "us",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): NewsApiResponse

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): NewsApiResponse
}