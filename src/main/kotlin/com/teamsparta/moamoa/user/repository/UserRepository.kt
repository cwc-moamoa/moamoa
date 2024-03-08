package com.teamsparta.moamoa.user.repository

import com.teamsparta.moamoa.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?
    fun findByIdAndDeletedAtIsNull(id: Long): Optional<User>

}
