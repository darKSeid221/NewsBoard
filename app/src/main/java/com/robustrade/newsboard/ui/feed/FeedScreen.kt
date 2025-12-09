package com.robustrade.newsboard.ui.feed

import androidx.compose.runtime.Composable
import com.robustrade.newsboard.domain.Article
import com.robustrade.newsboard.ui.FeedViewModel

// Compose
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// Paging Compose
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

// Hilt ViewModel
import androidx.hilt.navigation.compose.hiltViewModel

// Accompanist Swipe Refresh
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

// Components
import com.robustrade.newsboard.ui.components.ArticleCard
import com.robustrade.newsboard.ui.components.FullScreenError
import com.robustrade.newsboard.ui.components.FullScreenLoading
import com.robustrade.newsboard.ui.components.InlineError
import com.robustrade.newsboard.ui.components.ListItemLoading
import com.robustrade.newsboard.ui.components.SearchBar

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    onOpenArticle: (Article) -> Unit,
    onToggleBookmark: (Article) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val lazyPagingItems = viewModel.feed.collectAsLazyPagingItems()
    Scaffold(scaffoldState = scaffoldState) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            SearchBar(onSearch = { viewModel.setQuery(it) })
            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing =
            false), onRefresh = {
                lazyPagingItems.refresh()
            }) {
                LazyColumn {
                    items(lazyPagingItems.itemCount) { index ->
                        val article = lazyPagingItems[index]
                        article?.let {
                            ArticleCard(article = it,
                                onClick = { onOpenArticle(it) },
                                onBookmark = { onToggleBookmark(it) })
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