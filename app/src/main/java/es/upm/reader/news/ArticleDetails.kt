package es.upm.reader.news

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.upm.reader.news.model.Article
import es.upm.reader.news.serice.ArticlesService
import es.upm.reader.news.util.ImageUtils
import kotlinx.coroutines.launch

class ArticleDetails : AppCompatActivity() {

    private var article: Article? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)


        loadArticle();
        loadBackButton();
    }

    private fun loadBackButton() {
         (findViewById<View>(R.id.back_img) as ImageView).setOnClickListener(View.OnClickListener {
             finish()
        })
    }

    private fun loadArticle(){
        lifecycleScope.launch {
            article = ArticlesService.getArticle(4318)
            (findViewById<View>(R.id.title) as TextView).text = article?.title ?: "404 not found"
            if(article == null) return@launch

            //Load article info
            (findViewById<View>(R.id.subtitle) as TextView).text = article?.subtitle
            (findViewById<View>(R.id.view_category) as TextView).text = article?.category.toString()
            (findViewById<View>(R.id.view_abstract) as TextView).text = Html.fromHtml(article?.abstract, Html.FROM_HTML_MODE_COMPACT)
            (findViewById<View>(R.id.body) as TextView).text = Html.fromHtml(article?.body, Html.FROM_HTML_MODE_COMPACT)

            val articleImageView = findViewById<ImageView>(R.id.view_image)
            articleImageView?.setImageBitmap(article?.imageData?.let { ImageUtils.base64ToBitmap(it) })
            articleImageView.layoutParams.height = 500
        }
    }
}