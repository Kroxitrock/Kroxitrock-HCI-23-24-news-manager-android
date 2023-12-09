package es.upm.reader.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        authButton = findViewById<ImageView>(R.id.authButton)
        ApplicationProperties.loadProperties(baseContext.assets.open("application.properties"))
        articlesListView = findViewById(R.id.articlesList)

        handleAutoLogin()
        fetchArticles()
        handleAuthButton()
    }

    private fun handleAuthButton() {
        handleAuthButtonIcon()
        authButton.setOnClickListener{
            if (LoginService.isLoggedIn()) {
                LoginService.logout()
            } else {
                val loginActivityIntent = Intent(
                    this@MainActivity,
                    ArticleDetails::class.java
                )
                startActivity(loginActivityIntent)
            }
            handleAuthButtonIcon()
            fetchArticles()
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
                startActivity(articleDetailsIntent)
            }
        }
    }
}