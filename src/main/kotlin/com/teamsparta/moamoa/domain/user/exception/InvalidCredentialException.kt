package com.teamsparta.moamoa.domain.user.exception

data class InvalidCredentialException(
    override val message: String? = "The credential is invalid"
): RuntimeException()
