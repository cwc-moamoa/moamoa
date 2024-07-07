package com.teamsparta.moamoa.domain.search.model

import jakarta.persistence.*

@Entity
@Table(name = "searchHistory")
class SearchHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,
    val keyword: String,
    var count: Int,
)
