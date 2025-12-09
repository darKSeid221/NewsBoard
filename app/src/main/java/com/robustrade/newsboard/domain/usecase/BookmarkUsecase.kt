package com.robustrade.newsboard.domain.usecase

import com.robustrade.newsboard.domain.Article
import com.robustrade.newsboard.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class BookmarkUseCases(private val repo: NewsRepository)
{
    suspend fun add(article: Article) = repo.bookmarkArticle(article)
    suspend fun remove(article: Article) = repo.unbookmarkArticle(article)
    fun observe(): Flow<List<Article>> = repo.observeBookmarks()
    suspend fun isBookmarked(url: String) = repo.isBookmarked(url)
}