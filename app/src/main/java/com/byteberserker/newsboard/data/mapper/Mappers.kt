package com.byteberserker.newsboard.data.mapper

import com.byteberserker.newsboard.data.remote.ArticleDto
import com.byteberserker.newsboard.data.local.entity.ArticleEntity
import com.byteberserker.newsboard.data.local.entity.BookmarkEntity
import com.byteberserker.newsboard.domain.Article

fun ArticleDto.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        url = url ?: "",
        title = title ?: "No Title",
        description = description,
        content = content,
        imageUrl = urlToImage,
        sourceName = source?.name,
        publishedAt = publishedAt ?: "",
    )
}

fun ArticleEntity.toArticle(): Article {
    return Article(
        url = url,
        title = title,
        description = description,
        content = content,
        imageUrl = imageUrl,
        sourceName = sourceName,
        publishedAt = publishedAt
    )
}

fun BookmarkEntity.toArticle(): Article {
    return Article(
        url = url,
        title = title,
        description = description,
        content = content,
        imageUrl = imageUrl,
        sourceName = sourceName,
        publishedAt = publishedAt
    )
}

fun Article.toBookmarkEntity(): BookmarkEntity {
    return BookmarkEntity(
        url = url,
        title = title,
        description = description,
        content = content,
        imageUrl = imageUrl,
        sourceName = sourceName,
        publishedAt = publishedAt
    )
}
