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
    val scaffoldState = rememberScaffoldState()
    val lazyPagingItems = viewModel.feed.collectAsLazyPagingItems()
    val bookmarkedUrls by viewModel.bookmarkedUrls.collectAsState()

    Scaffold(scaffoldState = scaffoldState) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing =
            false), onRefresh = {
                lazyPagingItems.refresh()
            }) {
                LazyColumn {
                    items(lazyPagingItems.itemCount) { index ->
                        val article = lazyPagingItems[index]
                        article?.let {
                            val isBookmarked = bookmarkedUrls.contains(it.url)
                            ArticleCard(article = it,
                                onClick = { onOpenArticle(it) },
                                onBookmark = { viewModel.bookmarkArticle(it) },
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