package com.robustrade.newsboard.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robustrade.newsboard.data.local.BookmarkDao
import com.robustrade.newsboard.domain.Article

@Database(entities = [Article::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}