package com.teamsparta.moamoa.domain.user.repository

import com.teamsparta.moamoa.domain.user.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
}
