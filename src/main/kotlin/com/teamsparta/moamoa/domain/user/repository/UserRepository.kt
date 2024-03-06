package com.teamsparta.moamoa.domain.user.repository

import com.teamsparta.moamoa.domain.user.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
}
