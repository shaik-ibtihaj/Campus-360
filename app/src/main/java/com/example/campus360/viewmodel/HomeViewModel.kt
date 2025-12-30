package com.example.campus360.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.campus360.data.model.RecentSearch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(private val context: Context) : ViewModel() {
    private val sharedPreferences = context.getSharedPreferences("campus360_prefs", Context.MODE_PRIVATE)
    private val _recentSearches = MutableStateFlow<List<RecentSearch>>(emptyList())
    val recentSearches: StateFlow<List<RecentSearch>> = _recentSearches.asStateFlow()

    init {
        loadRecentSearches()
    }

    private fun loadRecentSearches() {
        val savedSearches = sharedPreferences.getString("recent_searches", "") ?: ""
        if (savedSearches.isNotEmpty()) {
            val searches = savedSearches.split("|")
                .filter { it.isNotEmpty() }
                .map { RecentSearch(it, 0L) }
            _recentSearches.value = searches
        } else {
            _recentSearches.value = emptyList()
        }
    }

    fun addSearchQuery(query: String) {
        if (query.isBlank()) return
        
        val currentList = _recentSearches.value.map { it.query }.toMutableList()
        // Remove existing to re-insert at top (de-duplicate)
        currentList.remove(query)
        currentList.add(0, query)
        
        // Limit to 8 items
        val limitedList = currentList.take(8)
        
        _recentSearches.value = limitedList.map { RecentSearch(it, System.currentTimeMillis()) }
        
        sharedPreferences.edit()
            .putString("recent_searches", limitedList.joinToString("|"))
            .apply()
    }

    fun clearSearchHistory() {
        _recentSearches.value = emptyList()
        sharedPreferences.edit()
            .remove("recent_searches")
            .apply()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
