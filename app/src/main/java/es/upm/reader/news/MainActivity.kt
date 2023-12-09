package es.upm.reader.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.upm.reader.news.adapter.ArticleAdapter
import es.upm.reader.news.model.Article
import es.upm.reader.news.serice.ArticlesService
import es.upm.reader.news.serice.LoginService
import es.upm.reader.news.util.ApplicationProperties
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var articlesListView: ListView
    private lateinit var authButton: ImageView

    private var articles: List<Article> = emptyList()

    private val onRefreshNeeded =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fetchArticles()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        authButton = findViewById(R.id.authButton)
        ApplicationProperties.loadProperties(baseContext.assets.open("application.properties"))
        articlesListView = findViewById(R.id.articlesList)

        handleAutoLogin()
        fetchArticles()
        handleAuthButton()
    }

    private fun handleAuthButton() {
        handleAuthButtonIcon()
        authButton.setOnClickListener {
            if (LoginService.isLoggedIn()) {
                LoginService.logout()
                fetchArticles()
            } else {
                val loginActivityIntent = Intent(
                    this@MainActivity,
                    Login::class.java
                )
                onRefreshNeeded.launch(loginActivityIntent)
            }
            handleAuthButtonIcon()
        }
    }

    private fun handleAuthButtonIcon() {
        if (LoginService.isLoggedIn()) {
            authButton.setImageResource(R.drawable.logout)
        } else {
            authButton.setImageResource(R.drawable.login)
        }
    }

    private fun handleAutoLogin() {
        if (!ApplicationProperties.getProperty("automatic_login").toBoolean()) {
            return
        }
        runBlocking {
            launch {
                LoginService.login(
                    ApplicationProperties.getProperty("username"),
                    ApplicationProperties.getProperty("password")
                )
            }
        }
    }

    private fun fetchArticles() {
        lifecycleScope.launch {
            articles = ArticlesService.getArticles() ?: emptyList()
            articlesListView.adapter = ArticleAdapter(this@MainActivity, articles)
            articlesListView.setOnItemClickListener { _, _, position, _ ->
                val articleId = articles[position].id
                val articleDetailsIntent = Intent(
                    this@MainActivity,
                    ArticleDetails::class.java
                )
                articleDetailsIntent.putExtra("articleId", articleId)
                onRefreshNeeded.launch(articleDetailsIntent)
            }
        }
    }
}