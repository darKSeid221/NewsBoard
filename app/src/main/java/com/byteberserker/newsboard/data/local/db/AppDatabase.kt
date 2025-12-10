package com.byteberserker.newsboard.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.byteberserker.newsboard.data.local.ArticleDao
import com.byteberserker.newsboard.data.local.BookmarkDao
import com.byteberserker.newsboard.data.local.entity.ArticleEntity
import com.byteberserker.newsboard.data.local.entity.BookmarkEntity

@Database(entities = [ArticleEntity::class, BookmarkEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun articleDao(): ArticleDao
}