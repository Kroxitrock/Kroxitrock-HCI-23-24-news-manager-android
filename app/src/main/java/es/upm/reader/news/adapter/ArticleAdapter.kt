package es.upm.reader.news.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.chip.Chip
import es.upm.reader.news.R
import es.upm.reader.news.model.Article
import es.upm.reader.news.util.ImageUtils

class ArticleAdapter(
    private val context: Context,
    private val articleList: List<Article>
) : BaseAdapter() {

    override fun getCount(): Int {
        return articleList.size
    }

    override fun getItem(position: Int): Any {
        return articleList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.article_card, parent, false)

        val article = articleList[position]

        val articleTitleView = view?.findViewById<TextView>(R.id.articleTitle)
        articleTitleView?.text = article.title

        if (article.category == null) {
            println("Bad category added by team ${article.username}")
        } else {
            val articleCategoryView = view?.findViewById<Chip>(R.id.articleCategory)
            articleCategoryView?.text = article.category.toString()
        }


        val articleAbstractView = view?.findViewById<TextView>(R.id.articleAbstract)
        articleAbstractView?.text = Html.fromHtml(article.abstract, Html.FROM_HTML_MODE_COMPACT)

        try {
            val articleImageView = view?.findViewById<ImageView>(R.id.articleImage)
            if (!article.thumbnailImage.isNullOrBlank()) {
                articleImageView?.setImageBitmap(ImageUtils.base64ToBitmap(article.thumbnailImage))
            } else {
                articleImageView?.setImageResource(R.drawable.no_image)
            }
        } catch (e: IllegalArgumentException) {
            println("Bad data added by team ${article.username}")
        }

        return view

    }
}