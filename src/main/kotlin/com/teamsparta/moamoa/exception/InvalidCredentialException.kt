package com.teamsparta.moamoa.exception

data class InvalidCredentialException(override val message: String? ) :
    RuntimeException()
