package com.example.demo.controller

import com.example.demo.models.Article
import com.example.demo.repository.ArticleRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(private val articleRepository: ArticleRepository) {

    @Cacheable("articles")
    @Transactional(readOnly = true)
    fun getAllArticles(pageable: Pageable): Page<Article> = articleRepository.findAll(pageable)

    @Cacheable("articles", key = "#id")
    @Transactional(readOnly = true)
    fun getArticleById(id: Long): Article? = articleRepository.findById(id).orElse(null)

    @Transactional
    fun createArticle(article: Article): Article = articleRepository.save(article)

    @Transactional
    fun updateArticle(id: Long, article: Article): Article? {
        return articleRepository.findById(id).map { existingArticle ->
            val updatedArticle = existingArticle.copy(title = article.title, content = article.content)
            articleRepository.save(updatedArticle)
        }.orElse(null)
    }

    @Transactional
    fun deleteArticle(id: Long) = articleRepository.deleteById(id)

    @Cacheable("articleSearch")
    @Transactional(readOnly = true)
    fun searchArticles(keyword: String): List<Article> = articleRepository.searchByTitle(keyword)
}