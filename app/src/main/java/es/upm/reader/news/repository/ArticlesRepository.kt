package es.upm.reader.news.repository

import es.upm.reader.news.model.Article
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticlesRepository {
    @GET("/pui-rest-news/articles")
    suspend fun getArticles(): Response<List<Article>>

    @GET("/pui-rest-news/article/{id}")
    suspend fun getArticles(@Path(value = "id") id: Int): Response<Article>
}