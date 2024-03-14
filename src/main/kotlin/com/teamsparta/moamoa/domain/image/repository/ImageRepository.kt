package com.teamsparta.moamoa.domain.image.repository

import com.teamsparta.moamoa.domain.image.model.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long>
