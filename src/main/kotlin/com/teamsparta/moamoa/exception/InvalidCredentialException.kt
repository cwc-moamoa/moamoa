package com.teamsparta.moamoa.exception

data class InvalidCredentialException(override val message: String? = "로그인 정보가 올바르지 않습니다.", ) :
    RuntimeException()
