package es.upm.reader.news.serice

import es.upm.reader.news.repository.ArticlesRepository
import es.upm.reader.news.repository.ArticlesRepositoryFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object ArticlesService {
    private val repository: ArticlesRepository = ArticlesRepositoryFactory().getInstance()

    fun getArticles() {
        runBlocking {
            launch {
                val result = repository.getArticles()
                println(result.body())
            }
        }
    }
}
