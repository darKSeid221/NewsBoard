package com.byteberserker.newsboard.ui.bookmark

import com.byteberserker.newsboard.domain.repository.BookmarkRepository
import com.byteberserker.newsboard.domain.Article
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import app.cash.turbine.test

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarkViewModelTest {

    private lateinit var repository: BookmarkRepository
    private lateinit var viewModel: BookmarkViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        every { repository.getAllBookmarks() } returns flowOf(emptyList())
        
        viewModel = BookmarkViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `checkBookmarkStatus updates state`() = runTest {
        val article = Article(
            url = "https://example.com",
            title = "Title",
            description = "Desc",
            content = "Content",
            imageUrl = null,
            sourceName = "Source",
            publishedAt = "Date"
        )
        coEvery { repository.isBookmarked("https://example.com") } returns true

        viewModel.checkBookmarkStatus(article)

        viewModel.isBookmarked.test {
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleBookmark saves bookmark if not bookmarked`() = runTest {
        val article = Article(
            url = "https://example.com",
            title = "Title",
            description = "Desc",
            content = "Content",
            imageUrl = null,
            sourceName = "Source",
            publishedAt = "Date"
        )
        coEvery { repository.isBookmarked("https://example.com") } returns false

        viewModel.toggleBookmark(article)

        coVerify { repository.saveBookmark(article) }
        viewModel.isBookmarked.test {
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

     @Test
    fun `toggleBookmark removes bookmark if bookmarked`() = runTest {
        val article = Article(
            url = "https://example.com",
            title = "Title",
            description = "Desc",
            content = "Content",
            imageUrl = null,
            sourceName = "Source",
            publishedAt = "Date"
        )
        coEvery { repository.isBookmarked("https://example.com") } returns true

        viewModel.toggleBookmark(article)

        coVerify { repository.removeBookmark(article) }
        viewModel.isBookmarked.test {
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
