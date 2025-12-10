package com.byteberserker.newsboard.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.byteberserker.newsboard.domain.Article
import com.byteberserker.newsboard.ui.detail.ArticleDetailScreen
import com.byteberserker.newsboard.ui.feed.FeedScreen
import com.byteberserker.newsboard.ui.profile.ProfileScreen

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Feed : Screen("feed", "Home", Icons.Default.Home)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Profile : Screen("profile", "Profile", Icons.Default.AccountCircle)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Feed,
        Screen.Search,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val showBottomBar = items.any { it.route == currentDestination?.route }
            if (showBottomBar) {
                BottomNavigation {
                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Feed.route, Modifier.padding(innerPadding)) {
            composable(Screen.Feed.route) {
                FeedScreen(
                    onOpenArticle = { article ->
                        CurrentArticleHolder.article = article
                        navController.navigate("detail")
                    }
                ) 
            }
            composable(Screen.Search.route) {
                com.byteberserker.newsboard.ui.search.SearchScreen(
                    onOpenArticle = { article ->
                        CurrentArticleHolder.article = article
                        navController.navigate("detail")
                    }
                )
            }
            composable(Screen.Profile.route) { 
                ProfileScreen(
                    onOpenArticle = { article ->
                        CurrentArticleHolder.article = article
                        navController.navigate("detail")
                    }
                ) 
            }
            
            composable("detail") {
                val article = CurrentArticleHolder.article
                if (article != null) {
                    val bookmarkViewModel: com.byteberserker.newsboard.ui.bookmark.BookmarkViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                    
                    androidx.compose.runtime.LaunchedEffect(article.url) {
                        bookmarkViewModel.checkBookmarkStatus(article)
                    }
                    val isBookmarked by bookmarkViewModel.isBookmarked.collectAsState()

                    ArticleDetailScreen(
                        article = article,
                        isBookmarked = isBookmarked,
                        onOpenExternal = { url ->
                             val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                             navController.context.startActivity(intent)
                        },
                        onShare = { art ->
                            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(android.content.Intent.EXTRA_TEXT, "${art.title}\n${art.url}")
                            }
                            navController.context.startActivity(android.content.Intent.createChooser(intent, "Share Article"))
                        },
                        onToggleBookmark = { art ->
                             bookmarkViewModel.toggleBookmark(art)
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

// Temporary holder to pass data between screens
object CurrentArticleHolder {
    var article: Article? = null
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}
