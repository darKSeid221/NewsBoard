package com.robustrade.newsboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.robustrade.newsboard.domain.Article
import com.robustrade.newsboard.domain.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repo: NewsRepository) : ViewModel() {
    private val _query = MutableStateFlow<String?>(null)

    // debounced search: upstream UI writes to setQuery
    fun setQuery(q: String?) {
        _query.value = q
    }

    val feed: StateFlow<PagingData<Article>> = _query
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { q -> repo.streamTopHeadlines(q) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
}