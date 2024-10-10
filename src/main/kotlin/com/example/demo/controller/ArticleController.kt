package com.example.demo.controller;

import com.example.demo.models.Article
import com.example.demo.repository.ArticleRepository
import jakarta.validation.Valid
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/articles")
class ArticleController(private val articleService: ArticleService) {

    @GetMapping
    fun getAllArticles(pageable: Pageable): ResponseEntity<Page<Article>> =
        ResponseEntity.ok(articleService.getAllArticles(pageable))

    @PostMapping
    fun createNewArticle(@Valid @RequestBody article: Article): ResponseEntity<Article> =
        ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(article))

    @GetMapping("/{id}")
    fun getArticleById(@PathVariable id: Long): ResponseEntity<Article> =
        articleService.getArticleById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PutMapping("/{id}")
    fun updateArticleById(@PathVariable id: Long, @Valid @RequestBody article: Article): ResponseEntity<Article> =
        articleService.updateArticle(id, article)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun deleteArticleById(@PathVariable id: Long): ResponseEntity<Void> {
        articleService.deleteArticle(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/search")
    fun searchArticles(@RequestParam keyword: String): ResponseEntity<List<Article>> =
        ResponseEntity.ok(articleService.searchArticles(keyword))
}
