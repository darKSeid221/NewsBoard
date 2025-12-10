package com.byteberserker.newsboard.domain.repository

import androidx.paging.PagingData
import com.byteberserker.newsboard.domain.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun streamTopHeadlines(query: String? = null): Flow<PagingData<Article>>
    suspend fun bookmarkArticle(article: Article)
    suspend fun unbookmarkArticle(article: Article)
    fun observeBookmarks(): Flow<List<Article>>
    suspend fun isBookmarked(url: String): Boolean
}