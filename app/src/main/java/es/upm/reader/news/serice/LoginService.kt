package es.upm.reader.news.serice

import es.upm.reader.news.model.Credentials
import es.upm.reader.news.model.User
import es.upm.reader.news.repository.LoginRepositoryFactory

object LoginService {
    private val repository = LoginRepositoryFactory().getInstance()

    private var user: User? = null

    suspend fun login(username: String, password: String) {

        val response = repository.login(Credentials(username, password))

        if (response.isSuccessful && response.body() != null) {
            user = response.body()
            return
        }

        throw Exception(response.message())
    }

    fun logout() {
        user = null
    }

    fun getAPIKey(): String? {
        return user?.apiKey
    }

    fun isLoggedIn(): Boolean {
        return user != null
    }


}