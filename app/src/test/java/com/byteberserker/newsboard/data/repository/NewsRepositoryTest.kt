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
        bookmarkDao = mockk(relaxed = true)
        database = mockk(relaxed = true)
        
        every { database.articleDao() } returns articleDao
        every { database.bookmarkDao() } returns bookmarkDao

        repository = NewsRepositoryImpl(newsApi, bookmarkDao, database, articleDao)
    }

    @Test
    fun `streamTopHeadlines returns flow of PagingData`() = runTest {
        val result = repository.streamTopHeadlines()
        assertNotNull(result)
    }
    
    @Test
    fun `removeBookmark calls dao delete`() = runTest {
        val article = Article(
            url = "https://example.com",
            title = "Test",
            description = "Desc",
            content = "Content",
            imageUrl = null,
            sourceName = "Source",
            publishedAt = "Date"
        )
        repository.unbookmarkArticle(article)
        coVerify { bookmarkDao.delete(any()) }
    }
}
