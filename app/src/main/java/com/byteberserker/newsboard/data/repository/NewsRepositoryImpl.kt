package com.byteberserker.newsboard.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.byteberserker.newsboard.data.local.ArticleDao
import com.byteberserker.newsboard.data.local.db.AppDatabase
import com.byteberserker.newsboard.data.mapper.toArticle
import com.byteberserker.newsboard.data.paging.ArticleRemoteMediator
import com.byteberserker.newsboard.data.remote.NewsApiService
import com.byteberserker.newsboard.domain.Article
import com.byteberserker.newsboard.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService,
    private val database: AppDatabase,
    private val articleDao: ArticleDao
) : NewsRepository {

    override fun streamTopHeadlines(query: String?): Flow<PagingData<Article>> {
        val searchQuery = query ?: ""
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            remoteMediator = ArticleRemoteMediator(
                query = searchQuery,
                newsApi = api,
                database = database,
                articleDao = articleDao
            ),
            pagingSourceFactory = { articleDao.pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toArticle() }
        }
    }
}