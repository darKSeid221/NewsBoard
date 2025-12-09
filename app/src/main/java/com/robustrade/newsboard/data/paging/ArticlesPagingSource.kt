package com.robustrade.newsboard.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.robustrade.newsboard.NewsApiService
import com.robustrade.newsboard.domain.Article

class ArticlesPagingSource(
    private val service: NewsApiService,
    private val query: String?
) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,
            Article> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        return try {
            val response = service.getTopHeadlines(page = page, pageSize =
            pageSize, query = query)
            val articles = response.articles.mapNotNull { dto ->
                dto.url?.let { url ->
                    Article(
                        url = url,
                        title = dto.title ?: ""
                        ,
                        description = dto.description,
                        content = dto.content,
                        imageUrl = dto.urlToImage,
                        sourceName = dto.source?.name,
                        publishedAt = dto.publishedAt
                    )
                }
            }
            5
            val nextKey = if (articles.isEmpty()) null else page + 1
            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}