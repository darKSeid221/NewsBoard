package com.robustrade.newsboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.robustrade.newsboard.domain.Article
import com.robustrade.newsboard.ui.FeedViewModel
import com.robustrade.newsboard.ui.detail.ArticleDetailScreen
import com.robustrade.newsboard.ui.feed.FeedScreen

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val context = androidx.compose.ui.platform.LocalContext.current
    var currentArticle by remember { mutableStateOf<Article?>(null) }
    
    // Hilt provides the ViewModel now
    // No factory needed


    if (currentArticle == null) {
        FeedScreen(
            onOpenArticle = { article ->
                currentArticle = article
            },
            onToggleBookmark = { /* TODO: Implement bookmark */ }
        )
    } else {
        ArticleDetailScreen(
            article = currentArticle!!,
            onOpenExternal = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            onShare = { article ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${article.title}\n${article.url}")
                }
                context.startActivity(Intent.createChooser(intent, "Share Article"))
            },
            onToggleBookmark = { /* TODO */ }
        )
    }
}