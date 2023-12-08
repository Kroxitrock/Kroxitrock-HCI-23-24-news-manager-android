package es.upm.reader.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import es.upm.reader.news.adapter.ArticleAdapter
import es.upm.reader.news.model.Article
import es.upm.reader.news.serice.ArticlesService
import es.upm.reader.news.util.ApplicationProperties
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var articlesListView: ListView
    private var articles: List<Article> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ApplicationProperties.loadProperties(baseContext.assets.open("application.properties"))
        articlesListView = findViewById<ListView>(R.id.articlesList)


        lifecycleScope.launch {
            articles = ArticlesService.getArticles() ?: emptyList()
            articlesListView.adapter = ArticleAdapter(this@MainActivity, articles)
        }


//        val btn = findViewById<View>(R.id.open_activity_button) as Button
//        btn.setOnClickListener {
//            startActivity(
//                Intent(
//                    this@MainActivity,
//                    ArticleDetails::class.java
//                )
//            )
//        }
    }
}