package com.robustrade.newsboard.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Article(
    @PrimaryKey val url: String,
    val title: String,
    val description: String?,
    val content: String?,
    val imageUrl: String?,
    val sourceName: String?,
    val publishedAt: String? // ISO-8601
)