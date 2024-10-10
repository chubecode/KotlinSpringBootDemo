package com.example.demo.repository

import com.example.demo.models.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Long>{
    @Query("SELECT a FROM Article a WHERE a.title LIKE %:keyword%")
    fun searchByTitle(@Param("keyword") keyword: String): List<Article>
}
