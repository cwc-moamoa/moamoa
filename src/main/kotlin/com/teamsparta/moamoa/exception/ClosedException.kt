package com.teamsparta.moamoa.exception

data class ClosedException(val modelName: String, val id: Any?) :
    RuntimeException("마감된 $modelName id : $id")
