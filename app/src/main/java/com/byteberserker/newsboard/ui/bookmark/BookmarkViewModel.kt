package com.byteberserker.newsboard.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteberserker.newsboard.domain.repository.BookmarkRepository
import com.byteberserker.newsboard.domain.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: BookmarkRepository
) : ViewModel() {

    val bookmarks = repository.getAllBookmarks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    private val _isBookmarked = kotlinx.coroutines.flow.MutableStateFlow(false)
    val isBookmarked: kotlinx.coroutines.flow.StateFlow<Boolean> = _isBookmarked

    fun checkBookmarkStatus(article: Article) {
        viewModelScope.launch {
            _isBookmarked.value = repository.isBookmarked(article.url)
        }
    }

    fun removeBookmark(article: Article) {
        viewModelScope.launch {
            repository.removeBookmark(article)
            _isBookmarked.value = false
        }
    }

    fun toggleBookmark(article: Article) {
        viewModelScope.launch {
            if (repository.isBookmarked(article.url)) {
                repository.removeBookmark(article)
                _isBookmarked.value = false
            } else {
                repository.saveBookmark(article)
                _isBookmarked.value = true
            }
        }
    }
}
