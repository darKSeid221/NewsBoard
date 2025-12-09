package com.robustrade.newsboard.ui.detail

import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.robustrade.newsboard.domain.Article

@Composable
fun ArticleDetailScreen(
    article: Article,
    onOpenExternal: (String) -> Unit,
    onShare: (Article) -> Unit,
    onToggleBookmark: (Article) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(article.sourceName ?: "Article") },
                actions = {
                    IconButton(onClick = { onShare(article) }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { onToggleBookmark(article) }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Bookmark")
                    }
                    IconButton(onClick = { onOpenExternal(article.url) }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Open")
                    }
                }
            )
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
                            .height(220.dp)
                    )
                }
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = article.publishedAt ?: "",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = article.content ?: article.description ?: "",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(8.dp)
                )
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