package com.byteberserker.newsboard.ui

import androidx.paging.PagingData
import com.byteberserker.newsboard.domain.Article
import com.byteberserker.newsboard.domain.repository.NewsRepository
import com.byteberserker.newsboard.domain.repository.BookmarkRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    private lateinit var repository: NewsRepository
    private lateinit var bookmarkRepository: BookmarkRepository
    private lateinit var viewModel: FeedViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        bookmarkRepository = mockk()
        every { bookmarkRepository.getAllBookmarks() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `articles flow is initialized from repository`() = runTest {
        val pagingData = PagingData.empty<Article>()
        every { repository.streamTopHeadlines() } returns flowOf(pagingData)
        viewModel = FeedViewModel(repository, bookmarkRepository)
        assertNotNull(viewModel.feed)
    }
}
