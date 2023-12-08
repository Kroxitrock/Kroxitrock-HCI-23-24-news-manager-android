package es.upm.reader.news.serice

import es.upm.reader.news.model.Article
import es.upm.reader.news.repository.ArticlesRepository
import es.upm.reader.news.repository.ArticlesRepositoryFactory

object ArticlesService {
    private val repository: ArticlesRepository = ArticlesRepositoryFactory().getInstance()

    suspend fun getArticles(): List<Article>? {
        val response = repository.getArticles()

        if (response.isSuccessful) {
            return response.body()
        }

        throw Exception(response.message())
    }
}
