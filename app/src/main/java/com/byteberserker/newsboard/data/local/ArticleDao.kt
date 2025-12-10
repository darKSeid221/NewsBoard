package com.byteberserker.newsboard.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.byteberserker.newsboard.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun insertAll(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Query("DELETE FROM articles")
    suspend fun clearAll()

    @Query("DELETE FROM articles WHERE url NOT IN (SELECT url FROM articles ORDER BY publishedAt DESC LIMIT :count)")
    suspend fun keepOnlyRecent(count: Int = 100)
}
