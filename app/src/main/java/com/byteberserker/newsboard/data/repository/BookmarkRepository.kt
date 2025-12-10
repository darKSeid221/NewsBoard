package com.byteberserker.newsboard.data.repository

import com.byteberserker.newsboard.domain.Article
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun getAllBookmarks(): Flow<List<Article>>
    suspend fun isBookmarked(url: String): Boolean
    suspend fun saveBookmark(article: Article)
    suspend fun removeBookmark(article: Article)
}
