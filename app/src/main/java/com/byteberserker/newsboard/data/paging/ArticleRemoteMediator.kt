package com.byteberserker.newsboard.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.byteberserker.newsboard.data.local.ArticleDao
import com.byteberserker.newsboard.data.local.db.AppDatabase
import com.byteberserker.newsboard.data.local.entity.ArticleEntity
import com.byteberserker.newsboard.data.mapper.toArticleEntity
import com.byteberserker.newsboard.data.remote.NewsApiService
import java.io.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val query: String,
    private val newsApi: NewsApiService,
    private val database: AppDatabase,
    private val articleDao: ArticleDao
) : RemoteMediator<Int, ArticleEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    val itemsCount = state.pages.sumOf { it.data.size }
                    val page = kotlin.math.ceil(itemsCount.toDouble() / state.config.pageSize).toInt() + 1
                    page
                }
            }

            val response = newsApi.searchNews(
                query = query.ifEmpty { "latest" },
                page = page,
                pageSize = state.config.pageSize
            )

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    articleDao.clearAll()
                }

                val articles = response.articles.map { dto ->
                    dto.toArticleEntity()
                }.filter { it.url.isNotEmpty() }

                articleDao.insertAll(articles)
                articleDao.keepOnlyRecent(100)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.articles.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
