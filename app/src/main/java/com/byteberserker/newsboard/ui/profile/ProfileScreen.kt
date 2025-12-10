package com.byteberserker.newsboard.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    viewModel: com.byteberserker.newsboard.ui.bookmark.BookmarkViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    onOpenArticle: (com.byteberserker.newsboard.domain.Article) -> Unit
) {
    val bookmarks by viewModel.bookmarks.collectAsState()

    androidx.compose.foundation.layout.Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(100.dp)
                        .background(MaterialTheme.colors.primary.copy(alpha = 0.2f), androidx.compose.foundation.shape.CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material.Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colors.primary
                    )
                }
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Kamal Majhi", style = MaterialTheme.typography.h5)
                Text(text = "kamalkumar4159@gmail.com", style = MaterialTheme.typography.body2, color = androidx.compose.ui.graphics.Color.Gray)
            }
        }
        
        androidx.compose.material.Divider()
        
        Text(
            text = "Bookmarks",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(bookmarks) { article ->
                com.byteberserker.newsboard.ui.components.ArticleCard(
                    article = article,
                    onClick = { onOpenArticle(article) },
                    onBookmark = { viewModel.removeBookmark(article) },
                    isBookmarked = true
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(100.dp)
                            .background(MaterialTheme.colors.primary.copy(alpha = 0.2f), androidx.compose.foundation.shape.CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "John Doe", style = MaterialTheme.typography.h5)
                    Text(text = "john.doe@example.com", style = MaterialTheme.typography.body2, color = androidx.compose.ui.graphics.Color.Gray)
                }
            }
            
            androidx.compose.material.Divider()
            
            Text(
                text = "Bookmarks",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
