package com.byteberserker.newsboard.ui.feed

import androidx.compose.runtime.Composable
import com.byteberserker.newsboard.domain.Article
import com.byteberserker.newsboard.ui.FeedViewModel

// Compose
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// Paging Compose
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems

// Hilt ViewModel
import androidx.hilt.navigation.compose.hiltViewModel

// Accompanist Swipe Refresh
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

// Components
import com.byteberserker.newsboard.ui.components.ArticleCard
import com.byteberserker.newsboard.ui.components.FullScreenError
import com.byteberserker.newsboard.ui.components.FullScreenLoading
import com.byteberserker.newsboard.ui.components.InlineError
import com.byteberserker.newsboard.ui.components.ListItemLoading

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    onOpenArticle: (Article) -> Unit
) {
    val lazyPagingItems = viewModel.feed.collectAsLazyPagingItems()
    val bookmarkedUrls by viewModel.bookmarkedUrls.collectAsState()

    FeedContent(
        lazyPagingItems = lazyPagingItems,
        bookmarkedUrls = bookmarkedUrls,
        onOpenArticle = onOpenArticle,
        onBookmark = { viewModel.bookmarkArticle(it) },
        onRefresh = { lazyPagingItems.refresh() }
    )
}

@Composable
fun FeedContent(
    lazyPagingItems: androidx.paging.compose.LazyPagingItems<Article>,
    bookmarkedUrls: Set<String>,
    onOpenArticle: (Article) -> Unit,
    onBookmark: (Article) -> Unit,
    onRefresh: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing =
            lazyPagingItems.loadState.refresh is LoadState.Loading), onRefresh = onRefresh) {
                LazyColumn {
                    items(lazyPagingItems.itemCount) { index ->
                        val article = lazyPagingItems[index]
                        article?.let {
                            val isBookmarked = bookmarkedUrls.contains(it.url)
                            ArticleCard(article = it,
                                onClick = { onOpenArticle(it) },
                                onBookmark = { onBookmark(it) },
                                isBookmarked = isBookmarked
                            )
                        }
                    }
// loading / error / empty
                    lazyPagingItems.apply {
                        when {
                            loadState.refresh is LoadState.Loading ->
                                item { FullScreenLoading() }
                            loadState.append is LoadState.Loading ->
                                item { ListItemLoading() }
                            loadState.refresh is LoadState.Error -> {
                                val e = loadState.refresh as LoadState.Error
                                item { FullScreenError(message =
                                e.error.localizedMessage ?: "Unknown") { retry() } }
                            }
                            loadState.append is LoadState.Error -> {
                                val e = loadState.append as LoadState.Error
                                item { InlineError(message =
                                e.error.localizedMessage ?: "Unknown") { retry() } }
                            }
                        }
                    }
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    // Mocking LazyPagingItems in preview is tricky, usually we just assume it renders the loading or empty state
    // or we use a flow of PagingData.
    // simpler approach:
    // We cannot easily instantiate LazyPagingItems in a preview without a composable context collecting a flow.
    // So we'll use a flow of empty data for the preview.
    val pagingData = kotlinx.coroutines.flow.flowOf(androidx.paging.PagingData.from(listOf(
        com.byteberserker.newsboard.domain.Article(
            url = "https://example.com/1",
            title = "Preview Article 1",
            description = "Description 1",
            content = "Content",
            imageUrl = null,
            sourceName = "Source 1",
            publishedAt = "2024-01-01"
        ),
        com.byteberserker.newsboard.domain.Article(
            url = "https://example.com/2",
            title = "Preview Article 2",
            description = "Description 2",
            content = "Content",
            imageUrl = null,
            sourceName = "Source 2",
            publishedAt = "2024-01-02"
        )
    )))
    val lazyPagingItems = pagingData.collectAsLazyPagingItems()

    MaterialTheme {
        FeedContent(
            lazyPagingItems = lazyPagingItems,
            bookmarkedUrls = emptySet(),
            onOpenArticle = {},
            onBookmark = {},
            onRefresh = {}
        )
    }
}