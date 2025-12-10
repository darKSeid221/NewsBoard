package com.byteberserker.newsboard.di

import android.content.Context
import androidx.room.Room
import com.byteberserker.newsboard.data.remote.NewsApiService
import com.byteberserker.newsboard.data.local.BookmarkDao
import com.byteberserker.newsboard.data.local.db.AppDatabase
import com.byteberserker.newsboard.data.repository.NewsRepositoryImpl
import com.byteberserker.newsboard.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level =
                HttpLoggingInterceptor.Level.BASIC
        }
        val retryInterceptor = Interceptor { chain ->
            var attempt = 0
            val max = 2
            var lastError: Exception? = null
            while (attempt <= max) {
                try {
                    return@Interceptor chain.proceed(chain.request())
                } catch (e: Exception) {
                    lastError = e
                    attempt++
                    if (attempt > max) throw e
                }
            }
            throw lastError ?: Exception("Unknown OkHttp error")
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(retryInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideNewsApi(retrofit: Retrofit): NewsApiService =
        retrofit.create(NewsApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "news_db"
        )
// addMigrations(...) in production
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideBookmarkDao(db: AppDatabase): BookmarkDao = db.bookmarkDao()

    @Provides
    fun provideArticleDao(db: AppDatabase): com.byteberserker.newsboard.data.local.ArticleDao = db.articleDao()

    @Provides
    @Singleton
    fun provideNewsRepository(
        api: NewsApiService,
        database: AppDatabase,
        articleDao: com.byteberserker.newsboard.data.local.ArticleDao
    ): NewsRepository = NewsRepositoryImpl(api, database, articleDao)
}