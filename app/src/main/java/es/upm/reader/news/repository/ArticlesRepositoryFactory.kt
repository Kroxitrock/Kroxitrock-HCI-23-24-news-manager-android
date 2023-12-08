package es.upm.reader.news.repository

import es.upm.reader.news.util.ApplicationProperties
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticlesRepositoryFactory {
    private val baseUrl = "http://sanger.dia.fi.upm.es/"

    fun getInstance(): ArticlesRepository {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient()).build()
            .create(ArticlesRepository::class.java)
    }

    private fun httpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val request: Request =
                chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "PUIRESTAUTH apikey=${ApplicationProperties.getProperty("api-key")}[\"api-key\"]")
                    .build()
            chain.proceed(request)
        }.build()
    }
}
