package es.upm.reader.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import es.upm.reader.news.serice.ArticlesService
import es.upm.reader.news.util.ApplicationProperties
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ApplicationProperties.loadProperties(baseContext.assets.open("application.properties"))
        lifecycleScope.launch {
            val articles = ArticlesService.getArticles()
            println(articles)
        }
    }
}