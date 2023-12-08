package es.upm.reader.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import es.upm.reader.news.model.Article
import es.upm.reader.news.serice.ArticlesService
import kotlinx.coroutines.launch

class ArticleDetails : AppCompatActivity() {

    private var article: Article? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        lifecycleScope.launch {
            article = ArticlesService.getArticle(1)
            (findViewById<View>(R.id.title) as TextView).text = article?.title ?: "404 not found"
            if(article == null) return@launch
            (findViewById<View>(R.id.subtitle) as TextView).text = article?.subtitle
            (findViewById<View>(R.id.view_category) as TextView).text = article?.category.toString()
            (findViewById<View>(R.id.view_abstract) as TextView).text = article?.abstract
            (findViewById<View>(R.id.body) as TextView).text = Html.fromHtml(article?.body)
        }

    }
}