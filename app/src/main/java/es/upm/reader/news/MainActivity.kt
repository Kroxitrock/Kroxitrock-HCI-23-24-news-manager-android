package es.upm.reader.news

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import es.upm.reader.news.adapter.ArticleAdapter
import es.upm.reader.news.model.Article
import es.upm.reader.news.model.Category
import es.upm.reader.news.model.User
import es.upm.reader.news.serice.ArticlesService
import es.upm.reader.news.serice.LoginService
import es.upm.reader.news.util.ApplicationProperties
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var articlesListView: ListView
    private lateinit var authButton: ImageView
    private lateinit var categoryFilter: LinearLayout

    private var articles: List<Article> = emptyList()
    private val onRefreshNeeded =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fetchArticles()
                handleAuthButtonIcon()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authButton = findViewById(R.id.authButton)
        ApplicationProperties.loadProperties(baseContext.assets.open("application.properties"))
        articlesListView = findViewById(R.id.articlesList)
        categoryFilter = findViewById(R.id.category_filter)

        LoginService.setSharedPrefs(getSharedPreferences(
            getString(R.string.user_credentials), Context.MODE_PRIVATE))
        LoginService.loadUser()

        handleAutoLogin()
        fetchArticles()
        handleAuthButton()
        handleFilters()
    }

    private fun handleFilters() {
        val inflater = LayoutInflater.from(this)
        val allChip = inflater.inflate(R.layout.category_chip, categoryFilter, false) as Chip
        allChip.setText(R.string.all)
        allChip.setOnClickListener{clearFilter()}
        categoryFilter.addView(allChip)
        Category.values().forEach { category ->
            val chip = inflater.inflate(R.layout.category_chip, categoryFilter, false) as Chip
            chip.text = category.toString()
            chip.setOnClickListener{filterBy(category)}
            categoryFilter.addView(chip)
        }
    }

    private fun filterBy(category: Category) {
        applyArticles(articles.filter { it.category == category })
    }

    private fun clearFilter() {
        applyArticles(articles)
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
                    ApplicationProperties.getProperty("password"),
                    false
                )
            }
        }
    }

    private fun fetchArticles() {
        lifecycleScope.launch {
            articles = ArticlesService.getArticles() ?: emptyList()
            applyArticles(articles)
        }
    }

    private fun applyArticles(articles: List<Article>) {
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