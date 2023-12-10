package es.upm.reader.news

import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.upm.reader.news.model.Article
import es.upm.reader.news.serice.ArticlesService
import es.upm.reader.news.serice.LoginService
import es.upm.reader.news.util.ImageUtils
import kotlinx.coroutines.launch

class ArticleDetails : AppCompatActivity() {

    private var updated = false
    private var article: Article? = null
    private lateinit var articleImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        articleImageView = findViewById<ImageView>(R.id.articleImage)

        loadArticle(intent.extras?.getInt("articleId"))
        loadBackButton()
        setupImage()
    }

    private fun setupImage() {
        if (!LoginService.isLoggedIn()) {
            return
        }

        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri == null) {
                    return@registerForActivityResult
                }
                articleImageView.setImageURI(uri)
                val imageBytes = contentResolver.openInputStream(uri)?.use { it.buffered().readBytes() }
                val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                val imageType = contentResolver.getType(uri)
                val newArticle = article?.copy(imageData = base64Image, imageMediaType = imageType)
                if (newArticle != null) {
                    lifecycleScope.launch {
                        ArticlesService.saveArticle(newArticle)
                        Toast.makeText(this@ArticleDetails, "Image updated",
                            Toast.LENGTH_SHORT).show()
                        updated = true
                    }
                }
            }

        articleImageView.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun loadBackButton() {
        findViewById<ImageView>(R.id.back_img).setOnClickListener {
            if (updated) {
                setResult(RESULT_OK)
            } else {
                setResult(RESULT_CANCELED)
            }
            finish()
        }
    }

    private fun loadArticle(articleId: Int?) {
        if (articleId == null) {
            return
        }
        lifecycleScope.launch {
            article = ArticlesService.getArticle(articleId)
            findViewById<TextView>(R.id.title).text = article?.title ?: "404 not found"
            if (article == null) return@launch

            //Load article info
            findViewById<TextView>(R.id.subtitle).text = article?.subtitle
            findViewById<TextView>(R.id.view_category).text = article?.category.toString()
            findViewById<TextView>(R.id.view_abstract).text =
                Html.fromHtml(article?.abstract, Html.FROM_HTML_MODE_COMPACT)
            findViewById<TextView>(R.id.body).text =
                Html.fromHtml(article?.body, Html.FROM_HTML_MODE_COMPACT)
            findViewById<TextView>(R.id.view_user).text = article?.username
            findViewById<TextView>(R.id.view_updated_date).text = article?.updateDate

            if (article?.imageData.isNullOrBlank()) {
                articleImageView.setImageResource(R.drawable.no_image)
            } else {
                articleImageView.setImageBitmap(article?.imageData?.let {
                    ImageUtils.base64ToBitmap(
                        it
                    )
                })
            }
            articleImageView.layoutParams.height = 500
        }
    }
}