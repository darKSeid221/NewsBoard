package com.byteberserker.newsboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val url: String,
    val title: String,
    val description: String?,
    val content: String?,
    val imageUrl: String?,
    val sourceName: String?,
    val publishedAt: String,
    val lastFetchedAt: Long = System.currentTimeMillis()
)
