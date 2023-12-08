package es.upm.reader.news.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import es.upm.reader.news.R
import es.upm.reader.news.model.Article
import es.upm.reader.news.util.ImageUtils
import java.lang.IllegalArgumentException

class ArticleAdapter (
    private val context: Context,
    private val articleList: List<Article>
): BaseAdapter() {

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
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.article_card, parent, false)

        val article = articleList[position]

        val articleTitleView = convertView?.findViewById<TextView>(R.id.articleTitle)
        articleTitleView?.text = article.title

        try {
            if (!article.thumbnailImage.isNullOrBlank() && !article.thumbnailMediaType.isNullOrBlank()) {
                val articleImageView = convertView?.findViewById<ImageView>(R.id.articleImage)
                articleImageView?.setImageBitmap(ImageUtils.base64ToBitmap(article.thumbnailImage))
            }
        } catch(e: IllegalArgumentException) {
            println("Bad data added by team ${article.username}")
        }

        return view

    }
}