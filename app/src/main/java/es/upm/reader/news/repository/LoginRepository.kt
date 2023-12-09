package es.upm.reader.news.repository

import es.upm.reader.news.model.Credentials
import es.upm.reader.news.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRepository {
    @POST("/pui-rest-news/login")
    suspend fun login(@Body credentials: Credentials): Response<User>
}