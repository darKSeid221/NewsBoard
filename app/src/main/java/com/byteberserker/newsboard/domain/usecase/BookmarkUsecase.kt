package com.byteberserker.newsboard.domain.usecase

import com.byteberserker.newsboard.domain.Article
import com.byteberserker.newsboard.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow

class BookmarkUseCases(private val repo: BookmarkRepository)
{
    suspend fun add(article: Article) = repo.saveBookmark(article)
    suspend fun remove(article: Article) = repo.removeBookmark(article)
    fun observe(): Flow<List<Article>> = repo.getAllBookmarks()
    suspend fun isBookmarked(url: String) = repo.isBookmarked(url)
}