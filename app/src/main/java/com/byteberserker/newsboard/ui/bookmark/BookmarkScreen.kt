package com.byteberserker.newsboard.ui.bookmark

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.byteberserker.newsboard.domain.Article
import com.byteberserker.newsboard.ui.components.ArticleCard

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    onOpenArticle: (Article) -> Unit
) {
    val bookmarks by viewModel.bookmarks.collectAsState()
    
    BookmarkContent(
        bookmarks = bookmarks,
        onOpenArticle = onOpenArticle,
        onRemoveBookmark = { viewModel.removeBookmark(it) }
    )
}

@Composable
fun BookmarkContent(
    bookmarks: List<Article>,
    onOpenArticle: (Article) -> Unit,
    onRemoveBookmark: (Article) -> Unit
) {
    if (bookmarks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No bookmarks yet", style = MaterialTheme.typography.h6)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = bookmarks,
                key = { it.url }
            ) { article ->
                ArticleCard(
                    article = article,
                    onClick = { onOpenArticle(article) },
                    onBookmark = { onRemoveBookmark(article) },
                    isBookmarked = true 
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun BookmarkScreenPreview() {
    MaterialTheme {
        BookmarkContent(
            bookmarks = listOf(
                com.byteberserker.newsboard.domain.Article(
                     url = "https://example.com/1",
                     title = "Bookmarked Article 1",
                     description = "Description 1",
                     content = "Content",
                     imageUrl = null,
                     sourceName = "Source 1",
                     publishedAt = "2024-01-01"
                 )
            ),
            onOpenArticle = {},
            onRemoveBookmark = {}
        )
    }
}
