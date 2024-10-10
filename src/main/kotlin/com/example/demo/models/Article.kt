package com.example.demo.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

@Entity
@Table(name = "articles", indexes = [Index(columnList = "title")])
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
data class Article(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:NotBlank
    @Column(nullable = false)
    val title: String = "",

    @field:NotBlank
    @Column(nullable = false, length = 10000)
    val content: String = ""
)
