package es.upm.reader.news.repository

import es.upm.reader.news.model.User
import retrofit2.Response
import retrofit2.http.POST

interface LoginRepository {
    @POST("/pui-rest-news/login")
    suspend fun getArticles(): Response<User>
}