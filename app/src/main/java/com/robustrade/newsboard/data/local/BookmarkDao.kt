package com.robustrade.newsboard.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robustrade.newsboard.domain.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)
    @Delete
    suspend fun delete(article: Article)
    @Query("SELECT * FROM bookmarks ORDER BY publishedAt DESC")
    fun getAllBookmarks(): Flow<List<Article>>
    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE url = :url)")
    suspend fun isBookmarked(url: String): Boolean
}