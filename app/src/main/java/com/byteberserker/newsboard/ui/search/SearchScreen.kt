package com.byteberserker.newsboard.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.byteberserker.newsboard.domain.Article
import com.byteberserker.newsboard.ui.FeedViewModel
import com.byteberserker.newsboard.ui.components.ArticleCard
import com.byteberserker.newsboard.ui.components.FullScreenError
import com.byteberserker.newsboard.ui.components.FullScreenLoading
import com.byteberserker.newsboard.ui.components.InlineError
import com.byteberserker.newsboard.ui.components.ListItemLoading
import com.byteberserker.newsboard.ui.components.SearchBar
import androidx.paging.LoadState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun SearchScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    onOpenArticle: (Article) -> Unit
) {
    val lazyPagingItems = viewModel.feed.collectAsLazyPagingItems()
    val bookmarkedUrls by viewModel.bookmarkedUrls.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    SearchContent(
        lazyPagingItems = lazyPagingItems,
        bookmarkedUrls = bookmarkedUrls,
        onSearch = { viewModel.setQuery(it) },
        onOpenArticle = onOpenArticle,
        onBookmark = { viewModel.bookmarkArticle(it) },
        onRefresh = { lazyPagingItems.refresh() },
        focusRequester = focusRequester
    )
}

@Composable
fun SearchContent(
    lazyPagingItems: androidx.paging.compose.LazyPagingItems<Article>,
    bookmarkedUrls: Set<String>,
    onSearch: (String) -> Unit,
    onOpenArticle: (Article) -> Unit,
    onBookmark: (Article) -> Unit,
    onRefresh: () -> Unit,
    focusRequester: FocusRequester
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            SearchBar(
                onSearch = onSearch,
                focusRequester = focusRequester
            )
            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing =
            lazyPagingItems.loadState.refresh is LoadState.Loading), onRefresh = onRefresh) {
                LazyColumn {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { it.url }
                    ) { index ->
                        val article = lazyPagingItems[index]
                        article?.let {
                            val isBookmarked = bookmarkedUrls.contains(it.url)
                            ArticleCard(
                                article = it,
                                onClick = { onOpenArticle(it) },
                                onBookmark = { onBookmark(it) },
                                isBookmarked = isBookmarked
                            )
                        }
                    }
                    lazyPagingItems.apply {
                        when {
                            loadState.refresh is LoadState.Loading ->
                                item { FullScreenLoading() }
                            loadState.append is LoadState.Loading ->
                                item { ListItemLoading() }
                            loadState.refresh is LoadState.Error -> {
                                val e = loadState.refresh as LoadState.Error
                                item { FullScreenError(message = e.error.localizedMessage ?: "Unknown") { retry() } }
                            }
                            loadState.append is LoadState.Error -> {
                                val e = loadState.append as LoadState.Error
                                item { InlineError(message = e.error.localizedMessage ?: "Unknown") { retry() } }
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
fun SearchScreenPreview() {
    val pagingData = kotlinx.coroutines.flow.flowOf(androidx.paging.PagingData.from(listOf(
        com.byteberserker.newsboard.domain.Article(
            url = "https://example.com/1",
            title = "Result Article 1",
            description = "Description 1",
            content = "Content",
            imageUrl = null,
            sourceName = "Source 1",
            publishedAt = "2024-01-01"
        )
    )))
    val lazyPagingItems = pagingData.collectAsLazyPagingItems()
    
    androidx.compose.material.MaterialTheme {
        SearchContent(
            lazyPagingItems = lazyPagingItems,
            bookmarkedUrls = emptySet(),
            onSearch = {},
            onOpenArticle = {},
            onBookmark = {},
            onRefresh = {},
            focusRequester = FocusRequester()
        )
    }
}
