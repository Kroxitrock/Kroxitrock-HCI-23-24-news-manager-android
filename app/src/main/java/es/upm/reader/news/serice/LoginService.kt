package es.upm.reader.news.serice

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.upm.reader.news.model.Credentials
import es.upm.reader.news.model.User
import es.upm.reader.news.repository.LoginRepositoryFactory
import kotlinx.coroutines.launch

object LoginService {
    private val repository = LoginRepositoryFactory().getInstance()
    private var sharedPref: SharedPreferences? = null

    private var user: User? = null

    suspend fun login(username: String, password: String, saveToPref: Boolean) {
        val response = repository.login(Credentials(username, password))

        if (response.isSuccessful && response.body() != null) {
            user = response.body()
            if(saveToPref) {
                saveToPref()
            }
            return
        }

        throw Exception(response.message())
    }

    fun logout() {
        user = null

        val edit: SharedPreferences.Editor = sharedPref!!.edit()
        edit.remove("username")
        edit.remove("apiKey")
        edit.apply()
    }

    fun getAPIKey(): String? {
        return user?.apiKey
    }

    fun getUser(): String? {
        return user?.apiKey
    }

    fun isLoggedIn(): Boolean {
        return user != null
    }

    fun setSharedPrefs(sharedPref: SharedPreferences) {
        this.sharedPref = sharedPref
    }

    private fun saveToPref() {
        val edit: SharedPreferences.Editor = sharedPref!!.edit()
        edit.putString("username", user?.username)
        edit.putString("apiKey", user?.apiKey)
        edit.apply()
    }

    fun loadUser() {
        val username: String? = sharedPref?.getString("username", null)
        val apiKey: String? = sharedPref?.getString("apiKey", null)
        if (username != null && apiKey != null) {
           this.user = User(username, apiKey)
        }

    }
}