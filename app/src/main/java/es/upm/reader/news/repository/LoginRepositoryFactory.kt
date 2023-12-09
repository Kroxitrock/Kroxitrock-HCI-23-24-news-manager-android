package es.upm.reader.news.repository

import es.upm.reader.news.util.ApplicationProperties
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginRepositoryFactory {
    fun getInstance(): LoginRepository {
        return Retrofit.Builder().baseUrl(ApplicationProperties.getProperty("base-url"))
            .addConverterFactory(GsonConverterFactory.create()).client(HttpClient.getInstance())
            .build()
            .create(LoginRepository::class.java)
    }
}