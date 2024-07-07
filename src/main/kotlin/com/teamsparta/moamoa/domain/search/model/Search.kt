package com.teamsparta.moamoa.domain.search.model

import com.teamsparta.moamoa.infra.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "search")
class Search(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "keyword", nullable = false)
    var keyword: String,
) : BaseTimeEntity()
