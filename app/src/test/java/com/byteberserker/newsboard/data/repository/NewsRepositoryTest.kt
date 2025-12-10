package com.byteberserker.newsboard.data.repository

import com.byteberserker.newsboard.data.remote.NewsApiService
import com.byteberserker.newsboard.data.local.ArticleDao
import com.byteberserker.newsboard.data.local.BookmarkDao
import com.byteberserker.newsboard.data.local.db.AppDatabase
import com.byteberserker.newsboard.domain.Article
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class NewsRepositoryTest {

    private lateinit var newsApi: NewsApiService
    private lateinit var articleDao: ArticleDao
    private lateinit var bookmarkDao: BookmarkDao
    private lateinit var database: AppDatabase
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setup() {
        newsApi = mockk()
        articleDao = mockk(relaxed = true)
        database = mockk(relaxed = true)
        
        every { database.articleDao() } returns articleDao

        repository = NewsRepositoryImpl(newsApi, database, articleDao)
    }

    @Test
    fun `streamTopHeadlines returns flow of PagingData`() = runTest {
        val result = repository.streamTopHeadlines()
        assertNotNull(result)
    }
}
