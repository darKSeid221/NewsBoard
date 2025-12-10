package com.byteberserker.newsboard.ui.detail

import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.byteberserker.newsboard.domain.Article

@Composable
fun ArticleDetailScreen(
    article: Article,
    isBookmarked: Boolean,
    onOpenExternal: (String) -> Unit,
    onShare: (Article) -> Unit,
    onToggleBookmark: (Article) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(article.sourceName ?: "Article") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onShare(article) }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { onToggleBookmark(article) }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isBookmarked) "Unbookmark" else "Bookmark"
                        )
                    }
                },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            Surface(
                elevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onOpenExternal(article.url) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp)
                ) {
                    Text("Read Full Article", style = MaterialTheme.typography.button)
                }
            }
        }
    ) { padding ->
        if (!article.content.isNullOrBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                article.imageUrl?.let { imageUrl ->
                    AndroidView(
                        factory = { ctx ->
                            ImageView(ctx).apply {
                                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                            }
                        },
                        update = { view ->
                            Glide.with(view.context)
                                .load(imageUrl)
                                .into(view)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                }
                
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = article.sourceName ?: "Unknown Source",
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.primary
                        )
                        Text(
                            text = article.publishedAt ?: "",
                            style = MaterialTheme.typography.caption
                        )
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    Divider()
                    Spacer(Modifier.height(16.dp))

                    val cleanContent = (article.content ?: article.description ?: "")
                        .replace(Regex("\\[\\+\\d+ chars\\]"), "")

                    Text(
                        text = cleanContent,
                        style = MaterialTheme.typography.body1.copy(lineHeight = 24.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(80.dp))
                }
            }
        } else {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        loadUrl(article.url)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ArticleDetailScreenPreview() {
    MaterialTheme {
        ArticleDetailScreen(
            article = com.byteberserker.newsboard.domain.Article(
                url = "https://example.com/article",
                title = "Breaking News: Major Development in Technology Sector",
                description = "A comprehensive look at the latest developments in the technology industry and what it means for the future.",
                content = "This is the full article content. It would normally contain several paragraphs of detailed information about the topic at hand. The content provides in-depth analysis and insights into the subject matter.",
                imageUrl = null,
                sourceName = "Tech News Daily",
                publishedAt = "2024-12-09T12:00:00Z"
            ),
            onOpenExternal = {},
            onShare = {},
            onToggleBookmark = {},
            onBack = {},
            isBookmarked = false
        )
    }
}