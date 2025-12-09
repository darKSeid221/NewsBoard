package com.robustrade.newsboard.data

import com.google.gson.annotations.SerializedName

data class NewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>
)

data class ArticleDto(
    val source: SourceDto?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    @SerializedName("urlToImage")
    val urlToImage: String?,
    @SerializedName("publishedAt")
    val publishedAt: String?,
    val content: String?
)
data class SourceDto(val id: String?, val name: String?)