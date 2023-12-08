package es.upm.reader.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.upm.reader.news.serice.ArticlesService
import es.upm.reader.news.util.ApplicationProperties

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ApplicationProperties.loadProperties(baseContext.assets.open("application.properties"))
        ArticlesService.getArticles()
    }
}