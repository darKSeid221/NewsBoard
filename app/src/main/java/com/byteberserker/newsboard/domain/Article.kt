package com.byteberserker.newsboard.domain


data class Article(
    val url: String,
    val title: String,
    val description: String?,
    val content: String?,
    val imageUrl: String?,
    val sourceName: String?,
    val publishedAt: String
)