package com.robustrade.newsboard.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.robustrade.newsboard.NewsApiService
import com.robustrade.newsboard.data.local.BookmarkDao
import com.robustrade.newsboard.data.paging.ArticlesPagingSource
import com.robustrade.newsboard.domain.Article
import com.robustrade.newsboard.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService,
    private val bookmarkDao: BookmarkDao
) : NewsRepository {
    override fun streamTopHeadlines(query: String?):
            Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { ArticlesPagingSource(api, query) }
        ).flow
    }
    override suspend fun bookmarkArticle(article: Article) =
        bookmarkDao.insert(article)
    override suspend fun unbookmarkArticle(article: Article) =
        bookmarkDao.delete(article)
    override fun observeBookmarks(): Flow<List<Article>> =
        bookmarkDao.getAllBookmarks()
    override suspend fun isBookmarked(url: String): Boolean =

    bookmarkDao.isBookmarked(url)
}