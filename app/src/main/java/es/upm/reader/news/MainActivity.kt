package es.upm.reader.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private var articles: List<Article> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ApplicationProperties.loadProperties(baseContext.assets.open("application.properties"))
        articlesListView = findViewById(R.id.articlesList)

        handleAutoLogin()
        fetchArticles()
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