package com.teamsparta.moamoa.domain.search.repository

import com.teamsparta.moamoa.domain.search.model.SearchHistory
import org.springframework.data.jpa.repository.JpaRepository

interface SearchHistoryRepository : JpaRepository<SearchHistory, Long> {
    fun findByKeyword(keyword: String): SearchHistory?
}
