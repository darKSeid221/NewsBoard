package com.byteberserker.newsboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.byteberserker.newsboard.domain.Article
import com.byteberserker.newsboard.domain.repository.NewsRepository
import com.byteberserker.newsboard.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repo: NewsRepository,
    private val bookmarkRepo: BookmarkRepository
) : ViewModel() {
    private val _query = MutableStateFlow<String?>(null)

    fun setQuery(q: String?) {
        _query.value = q
    }

    val feed: StateFlow<PagingData<Article>> = _query
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { q -> repo.streamTopHeadlines(q) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    val bookmarkedUrls: StateFlow<Set<String>> = bookmarkRepo.getAllBookmarks()
        .map { list -> list.map { it.url }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    fun bookmarkArticle(article: Article) {
        viewModelScope.launch {
            if (bookmarkRepo.isBookmarked(article.url)) {
                bookmarkRepo.removeBookmark(article)
            } else {
                bookmarkRepo.saveBookmark(article)
            }
        }
    }
}