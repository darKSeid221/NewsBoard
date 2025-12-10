package com.byteberserker.newsboard.data.repository

import com.byteberserker.newsboard.data.local.BookmarkDao
import com.byteberserker.newsboard.domain.Article
import com.byteberserker.newsboard.data.mapper.toArticle
import com.byteberserker.newsboard.data.mapper.toBookmarkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {
    override fun getAllBookmarks(): Flow<List<Article>> {
        return bookmarkDao.getAllBookmarks().map { entities ->
            entities.map { it.toArticle() }
        }
    }

    override suspend fun isBookmarked(url: String): Boolean {
        return bookmarkDao.isBookmarked(url)
    }

    override suspend fun saveBookmark(article: Article) {
        bookmarkDao.insert(article.toBookmarkEntity())
    }

    override suspend fun removeBookmark(article: Article) {
        bookmarkDao.delete(article.toBookmarkEntity())
    }
}
