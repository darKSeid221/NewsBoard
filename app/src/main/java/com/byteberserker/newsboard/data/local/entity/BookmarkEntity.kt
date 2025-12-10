package com.byteberserker.newsboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val url: String,
    val title: String,
    val description: String?,
    val content: String?,
    val imageUrl: String?,
    val sourceName: String?,
    val publishedAt: String,
    val bookmarkedAt: Long = System.currentTimeMillis()
)
