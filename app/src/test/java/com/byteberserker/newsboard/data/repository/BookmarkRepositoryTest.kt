package com.byteberserker.newsboard.data.repository

import com.byteberserker.newsboard.data.local.BookmarkDao
import com.byteberserker.newsboard.data.local.entity.BookmarkEntity
import com.byteberserker.newsboard.domain.Article
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import app.cash.turbine.test

class BookmarkRepositoryTest {

    private lateinit var bookmarkDao: BookmarkDao
    private lateinit var repository: BookmarkRepositoryImpl

    @Before
    fun setup() {
        bookmarkDao = mockk(relaxed = true)
        repository = BookmarkRepositoryImpl(bookmarkDao)
    }

    @Test
    fun `getAllBookmarks emits mapped articles`() = runTest {
        val entity = BookmarkEntity(
            url = "https://example.com",
            title = "Title",
            description = "Desc",
            content = "Content",
            imageUrl = null,
            sourceName = "Source",
            publishedAt = "Date"
        )
        every { bookmarkDao.getAllBookmarks() } returns flowOf(listOf(entity))

        repository.getAllBookmarks().test {
            val articles = awaitItem()
            assertEquals(1, articles.size)
            assertEquals("Title", articles[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `saveBookmark calls dao insert`() = runTest {
        val article = Article(
            url = "https://example.com",
            title = "Title",
            description = "Desc",
            content = "Content",
            imageUrl = null,
            sourceName = "Source",
            publishedAt = "Date"
        )
        repository.saveBookmark(article)

        coVerify { bookmarkDao.insert(any()) }
    }
}
