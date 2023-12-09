package es.upm.reader.news.repository

import es.upm.reader.news.serice.LoginService
import es.upm.reader.news.util.ApplicationProperties
import okhttp3.OkHttpClient
import okhttp3.Request

object HttpClient {
    fun getInstance() = OkHttpClient.Builder().addInterceptor { chain ->
        val request: Request =
            chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader(
                    "Authorization",
                    "PUIRESTAUTH apikey=${getAPIKey()}"
                )
                .build()
        chain.proceed(request)
    }.build()

    private fun getAPIKey(): String {
        if (LoginService.isLoggedIn()) {
            return LoginService.getAPIKey() ?: ApplicationProperties.getProperty("api-key")
        }

        return ApplicationProperties.getProperty("api-key")
    }
}