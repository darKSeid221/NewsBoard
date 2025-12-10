package com.byteberserker.newsboard

import com.byteberserker.newsboard.data.remote.NewsApiService
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: NewsApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getTopHeadlines returns list of articles on success`() = runTest {
        val mockResponse = """
            {
              "status": "ok",
              "totalResults": 1,
              "articles": [
                {
                  "source": { "id": "cnn", "name": "CNN" },
                  "author": "John Doe",
                  "title": "Test Headline",
                  "description": "Test Description",
                  "url": "https://example.com",
                  "urlToImage": "https://example.com/image.jpg",
                  "publishedAt": "2024-01-01T12:00:00Z",
                  "content": "Test Content"
                }
              ]
            }
        """.trimIndent()
        
        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        val response = apiService.getTopHeadlines(page = 1)

        assertEquals("ok", response.status)
        assertEquals(1, response.totalResults)
        assertEquals(1, response.articles.size)
        
        val article = response.articles[0]
        assertEquals("CNN", article.source?.name)
        assertEquals("Test Headline", article.title)
    }

    @Test
    fun `searchNews returns list of articles on success`() = runTest {
        val mockResponse = """
            {
              "status": "ok",
              "totalResults": 1,
              "articles": [
                {
                  "source": { "id": null, "name": "TechCrunch" },
                  "author": "Jane Smith",
                  "title": "Tech News",
                  "description": "Tech Description",
                  "url": "https://techcrunch.com",
                  "urlToImage": null,
                  "publishedAt": "2024-01-02T12:00:00Z",
                  "content": "Tech Content"
                }
              ]
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        val response = apiService.searchNews(query = "tech", page = 1)

        assertEquals(1, response.articles.size)
        assertEquals("TechCrunch", response.articles[0].source?.name)
    }
}
