package es.upm.reader.news.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticlesRepositoryFactory {
    private val baseUrl = "http://sanger.dia.fi.upm.es/"

    fun getInstance(): ArticlesRepository {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).client(HttpClient.getInstance())
            .build()
            .create(ArticlesRepository::class.java)
    }
}
